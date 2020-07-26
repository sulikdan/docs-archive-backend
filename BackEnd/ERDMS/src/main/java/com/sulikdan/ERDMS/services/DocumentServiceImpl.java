package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
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
  protected final OCRService ocrService;
  protected final VirtualStorageService virtualStorageService;
  protected final FileStorageService fileStorageService;

  // repos
  protected final DocumentRepository documentRepository;

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

    if (document.getDocConfig().getScanImmediately()) {
      // send request to OCR-API
      Document sentToOCR = ocrService.extractTextFromDocument(document, document.getDocConfig());
      // save results to queue & to be later processed
      virtualStorageService.addDocument(sentToOCR);
      // save to DB
      saveDocument(sentToOCR);
      // return result
      return sentToOCR;
    } else {

      // save to DB
      saveDocument(document);
      // return result
      return document;
    }
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
  protected static String generateNamePrefix() {
    Date now = new Date();
    return "ERDMS_" + now.getTime();
  }
}
