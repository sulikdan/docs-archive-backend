package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.exceptions.DocNotFoundException;
import com.sulikdan.ERDMS.exceptions.InvalidAccessRightException;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import com.sulikdan.ERDMS.workers.OcrApiJobWorker;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
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
 * @see DocService
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

  private final MongoTemplate mongoTemplate;

  private final Set<String> mapSortColumns;

  public DocServiceImpl(
      TaskExecutor taskExecutor,
      OCRService ocrService,
      VirtualStorageService virtualStorageService,
      FileStorageService fileStorageService,
      DocRepository documentRepository,
      MongoTemplate mongoTemplate) {
    this.taskExecutor = taskExecutor;
    this.ocrService = ocrService;
    this.virtualStorageService = virtualStorageService;
    this.fileStorageService = fileStorageService;
    this.documentRepository = documentRepository;
    this.mongoTemplate = mongoTemplate;

    String[] columnsChoices = {"id", "state", "language", "createddatetime", "updateddatetime"};
    mapSortColumns = new HashSet<>(Arrays.asList(columnsChoices));

    TextIndexDefinition textIndex =
        new TextIndexDefinition.TextIndexDefinitionBuilder().onAllFields().build();
    mongoTemplate.indexOps(Doc.class).ensureIndex(textIndex);
  }

  @Override
  public Doc findDocById(String id, User user) {
    Optional<Doc> optionalDocument = documentRepository.findById(id);

    if (!optionalDocument.isPresent()) {
      log.warn(MessageFormat.format("Document with id={} not found!", id));
      throw new DocNotFoundException("Doc mot found");
    }

    Doc foundDoc = optionalDocument.get();

    //  Check access rights!
    if (!foundDoc.getIsShared() && !foundDoc.getOwner().getId().equals(user.getId())) {
      log.warn(MessageFormat.format("Accessing doc with id={}, but no rights!", id));
      throw new DocNotFoundException("Doc mot found");
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
  public List<Doc> processNewDocs(MultipartFile[] files, DocConfig docConfig, User user)
      throws IOException {
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
              .owner(user)
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
  public void deleteDocById(String id, User user) {
    final Optional<Doc> found = documentRepository.findById(id);
    if (found.isPresent()) {
      if (found.get().getOwner().getId().equals(user.getId())) documentRepository.deleteById(id);
      else throw new InvalidAccessRightException("You don't have rights to delete the document!");
    } else {
      throw new DocNotFoundException("Document not found!");
    }
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
    doc.setUpdateDateTime(LocalDateTime.now());
    return documentRepository.save(doc);
  }

  //  @Transactional
  @Override
  public void updateDoc(Doc doc, User user) {

    Optional<Doc> foundDocOptional = documentRepository.findById(doc.getId());
    if (!foundDocOptional.isPresent()) {
      throw new DocNotFoundException("Requested document for update was not found!");
    } else if (!foundDocOptional.get().getOwner().getId().equals(user.getId())) {
      throw new InvalidAccessRightException("You don't have rights to update the document!");
    }

    Doc foundDoc = foundDocOptional.get();
    doc.setNameOfFile(foundDoc.getNameOfFile());
    doc.setOwner(foundDoc.getOwner());
    //    TODO do other params + should check null values ?

    saveDoc(doc);
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
      SearchDocParams searchDocParams, Integer page, Integer size, User user) {

    searchDocParams.setPageIndex(page);
    searchDocParams.setPageSize(size);

    checkSortColumn(searchDocParams);

    Page<Doc> foundDocPages;
    if (searchDocParams.getFullText() != null && !searchDocParams.getFullText().isEmpty()) {

      PageRequest pageRequest = PageRequest.of(searchDocParams.getPageIndex(), searchDocParams.getPageSize());
      Query query =
          TextQuery.query(
                  TextCriteria.forDefaultLanguage().matchingAny(searchDocParams.getFullText()))
              .with(pageRequest);
      Criteria orCriteria = new Criteria();

      query.addCriteria(orCriteria.orOperator(Criteria.where("owner").is(user),(Criteria.where("isShared").is(Boolean.TRUE))));
      List<Doc> list = mongoTemplate.find(query, Doc.class);
      long count = mongoTemplate.count(query, Doc.class);
      foundDocPages = new PageImpl<Doc>(list , pageRequest, count);

    } else {
      foundDocPages = documentRepository.findDocsByMultipleArgs(searchDocParams, user);
    }
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
