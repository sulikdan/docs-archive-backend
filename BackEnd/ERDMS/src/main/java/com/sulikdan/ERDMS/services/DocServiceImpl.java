package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import com.sulikdan.ERDMS.workers.OcrApiJobWorker;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
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
public class DocServiceImpl implements DocService {

  private TaskExecutor taskExecutor;

  // services
  private final OCRService ocrService;
  private final VirtualStorageService virtualStorageService;
  private final FileStorageService fileStorageService;

  // repos
  private final DocRepository documentRepository;

  private final Set<String> mapSortColumns;

  public DocServiceImpl(
      TaskExecutor taskExecutor,
      OCRService ocrService,
      VirtualStorageService virtualStorageService,
      FileStorageService fileStorageService,
      DocRepository documentRepository) {
    this.taskExecutor = taskExecutor;
    this.ocrService = ocrService;
    this.virtualStorageService = virtualStorageService;
    this.fileStorageService = fileStorageService;
    this.documentRepository = documentRepository;

    String[] columnsChoices = {"id", "state", "language", "createddatetime", "updateddatetime"};
    mapSortColumns = new HashSet<>(Arrays.asList(columnsChoices));
  }

  @Override
  public Doc findDocById(String id) {
    Optional<Doc> optionalDocument = documentRepository.findById(id);

    if (!optionalDocument.isPresent()) {
      log.warn(MessageFormat.format("Document with id={} not found!", id));
      // TODO
      throw new RuntimeException("Not found");
    }

    return optionalDocument.get();
  }

  @Override
  public List<Doc> findAllDocs() {
    List<Doc> docList = new ArrayList<>();
    documentRepository.findAll().forEach(docList::add);
    return docList;
  }

  @Override
  public List<Doc> processNewDocs(MultipartFile[] files, DocConfig docConfig) throws IOException {
    List<Doc> uploadedDocs = new ArrayList<>();

    for (MultipartFile file : files) {

      log.info("Processing file: ");
      Doc docToProcess =
          Doc.builder()
              .nameOfFile(file.getOriginalFilename())
              .documentAsBytes(file.getBytes())
              .documentPreview(createThumbnail(file, 150).toByteArray())
              .docConfig(docConfig)
              .asyncApiInfo(
                  new AsyncApiInfo(
                      docConfig.getScanImmediately()
                          ? AsyncApiState.MANUAL_SENDING
                          : AsyncApiState.WAITING_TO_SEND,
                      null,
                      null))
              .build();

      uploadedDocs.add(docToProcess);
      // TODO consider to send all files together not seperately && not atomic in mongoDB if send
      // together
      createNewDoc(docToProcess);
    }

    // TODO change returned DOCS to DTO
    return uploadedDocs;
  }

  @Override
  public void deleteDocumentById(String id) {
    documentRepository.deleteById(id);
  }

  @Override
  public Doc createNewDoc(Doc doc) {

    Doc saved = saveDoc(doc);
    //    documentRepository.save(document);

    // Scan doc job
    if (doc.getDocConfig().getScanImmediately()) {
      virtualStorageService.addDoc(doc.getId());
      taskExecutor.execute(
          new OcrApiJobWorker(ocrService, this, documentRepository, virtualStorageService, doc));
    }

    return saved;
  }

  //  @Transactional
  //  https://docs.mongodb.com/manual/core/write-operations-atomicity/
  @Override
  public Doc saveDoc(Doc doc) {
    //    TODO very dumb idea, but mongoSucks
    doc.setUpdateDateTime(LocalDateTime.now());
    return documentRepository.save(doc);
  }

  //  @Transactional
  @Override
  public void updateDocument(Doc doc) {
    //      TODO necessary? saveDocument should be enought
    doc.setUpdateDateTime(LocalDateTime.now());
    documentRepository.save(doc);
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
   *
   * @param asyncApiState
   * @return
   */
  @Override
  public List<Doc> finDocumentsByAsyncApiState(AsyncApiState asyncApiState) {
    return StreamSupport.stream(documentRepository.findAll().spliterator(), false)
        .filter(document -> document.getAsyncApiInfo().getAsyncApiState().equals(asyncApiState))
        .collect(Collectors.toList());
  }

  @Override
  public Page<Doc> findDocsUsingSearchParams(
      SearchDocParams searchDocParams, Integer page, Integer size) {

    searchDocParams.setPageIndex(page);
    searchDocParams.setPageSize(size);

    checkSortColumn(searchDocParams);

    Page<Doc> foundDocPages = documentRepository.findDocsByMultipleArgs(searchDocParams);

    return foundDocPages;
  }

  private void checkSortColumn(SearchDocParams searchDocParams) {

    searchDocParams.setColumnSortList(
        searchDocParams.getColumnSortList().stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList()));

    if (searchDocParams.getColumnSortList().size() > mapSortColumns.size()) {
      //      TODO error
      throw new RuntimeException("TODO later");
    }

    Set<String> tmpSet = new HashSet<>(searchDocParams.getColumnSortList());
    if (tmpSet.size() < searchDocParams.getColumnSortList().size())
      throw new RuntimeException("Wrong input, duplicates found");

    for (String column : searchDocParams.getColumnSortList()) {
      if (!mapSortColumns.contains(column))
        throw new RuntimeException("Bad request - unsupported column.");
    }
  }

  private ByteArrayOutputStream createThumbnail(MultipartFile orginalFile, Integer width)
      throws IOException {
    //    ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
    //    BufferedImage thumbImg = null;
    //    BufferedImage img = ImageIO.read(orginalFile.getInputStream());
    //    thumbImg =
    //        Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width,
    // Scalr.OP_ANTIALIAS);
    //    ImageIO.write(thumbImg, orginalFile.getContentType().split("/")[1], thumbOutput);
    //    log.info("--"+ thumbOutput.toByteArray().toString() +"--");
    //    return thumbOutput;
    //    BufferedImage img = ImageIO.read(orginalFile.getInputStream());
    //    ByteArrayOutputStream imgStrema;
    ////    orginalFile.
    //    return  Thumbnails.of(img).scale(0.25).asBufferedImage();
    String extension = FilenameUtils.getExtension(orginalFile.getOriginalFilename());

    log.warn("Content type" + orginalFile);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    BufferedImage originalImage = ImageIO.read(orginalFile.getInputStream());

    Thumbnails.of(originalImage)
        .size(200, 200)
        .outputFormat(extension)
        .toOutputStream(outputStream);
    return outputStream;
  }
}
