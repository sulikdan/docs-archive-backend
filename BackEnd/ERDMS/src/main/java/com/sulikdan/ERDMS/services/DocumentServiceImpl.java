package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class DocumentServiceImpl is implementation of DocumentService interface.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

  // services
  private final OCRService ocrService;
  private final VirtualStorageService virtualStorageService;
  private final FileStorageService fileStorageService;

  // repos
  private final DocumentRepository documentRepository;

  public DocumentServiceImpl(
      OCRService ocrService,
      VirtualStorageService virtualStorageService,
      FileStorageService fileStorageService,
      DocumentRepository documentRepository) {
    this.ocrService = ocrService;
    this.virtualStorageService = virtualStorageService;
    this.fileStorageService = fileStorageService;
    this.documentRepository = documentRepository;
  }

  @Override
  public Document findDocumentById(String id) {
    Optional<Document> optionalDocument = documentRepository.findById(id);

    if (!optionalDocument.isPresent()) {
      log.warn(MessageFormat.format("Document with id={} not found!", id));
      // TODO
      throw new RuntimeException("Not found");
    }

    return optionalDocument.get();
  }

  @Override
  public List<Document> findAllDocuments() {
    List<Document> documentList = new ArrayList<>();
    documentRepository.findAll().forEach(documentList::add);
    return documentList;
  }

  @Override
  public List<Document> processNewDocuments(MultipartFile[] files, DocConfig docConfig)
      throws IOException {
    List<Document> uploadedDocs = new ArrayList<>();

    for (MultipartFile file : files) {
      Path savedFilePath = fileStorageService.saveFile(file, generateNamePrefix());

      Document docToProcess =
          new Document(
              file.getName(), savedFilePath, ArrayUtils.toObject(file.getBytes()), docConfig);

      uploadedDocs.add(docToProcess);
      // TODO consider to send all files together not seperately
      createNewDocument(docToProcess);
    }

    // TODO change returned DOCS to DTO
    return uploadedDocs;
  }

  @Override
  public Document createNewDocument(Document document) {

    // save to DB
    // TODO consider using only repository?
    Document saved = saveDocument(document);

    // This part should be done asyncly ... or at least partially
    if (document.getDocConfig().getScanImmediately()) {
      // send request to OCR-API
      Document sentToOCR = ocrService.extractTextFromDocument(saved, document.getDocConfig());
      // save results to queue & to be later processed
      virtualStorageService.addDocument(sentToOCR);
      // update doc
      // TODO consider using only repository?
      updateDocument(sentToOCR);
    }

    return saved;
  }

  @Override
  public Document saveDocument(Document document) {
    return documentRepository.save(document);
  }

  @Override
  public void updateDocument(Document document) {
    //      TODO it's not best impl... should check saving and overriding of element values ..
    documentRepository.save(document);
  }

  /**
   * Generates name prefix for uploaded files. consisting of OCR_Timestamp
   *
   * @return strings "OCR_" + "current_timestamp_now()"
   * @implNote It's temporary solution and for many threaded usage, there may chance of collision
   *     and may need to be tweaked with adding thread number to it.
   */
  private static String generateNamePrefix() {
    Date now = new Date();
    return "ERDMS_" + now.getTime();
  }

  /**
   * Temporary || easiest solution
   * @param asyncApiState
   * @return
   */
  @Override
  public List<Document> finDocumentsByAsyncApiState(AsyncApiState asyncApiState) {
    return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
        .filter(document -> document.getAsyncApiInfo().getAsyncApiState().equals(asyncApiState))
        .collect(Collectors.toList());
  }
}
