package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentMongoRepository;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.VirtualStorageService;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Daniel Šulik on 25-Jul-20
 *
 * <p>Class DocumentOcrChecker is used for .....
 */
@Slf4j
@Configuration
@EnableScheduling
public class DocumentOcrChecker {

  private TaskExecutor taskExecutor;

  private final BeanFactory beanFactory;

  private final DocumentService documentService;
  private final DocumentMongoRepository documentMongoRepository;
  private final DocumentRepository documentRepository;
  private final VirtualStorageService virtualStorageService;

  public DocumentOcrChecker(
      TaskExecutor taskExecutor,
      BeanFactory beanFactory,
      DocumentService documentService,
      DocumentMongoRepository documentMongoRepository,
      DocumentRepository documentRepository,
      VirtualStorageService virtualStorageService) {
    this.taskExecutor = taskExecutor;
    this.beanFactory = beanFactory;
    this.documentService = documentService;
    this.documentMongoRepository = documentMongoRepository;
    this.documentRepository = documentRepository;
    this.virtualStorageService = virtualStorageService;
  }

  // 2min -> 120000milis
  @Scheduled(fixedDelay = 60000) // (120000/2), initialDelay = (120000/2))
  public void checkUnscannedDocuments() {
    log.info("Started DocumentOcrChecker!");

    //    TODO add pageLimit ...
    // find completed -> to be deleted? -- may not be needed
    List<Document> cleaningDocs =
        documentRepository.findDocumentsByAsyncApiInfoAsyncApiState(
            AsyncApiState.RESOURCE_TO_CLEAN);
    //        documentService.finDocumentsByAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

    // find processed -> to download
    List<Document> completedDocs =
        documentRepository.findDocumentsByAsyncApiInfoAsyncApiState(AsyncApiState.SCANNED);
    //        documentService.finDocumentsByAsyncApiState(AsyncApiState.SCANNED);

    //    check status
    List<Document> processingDocs =
        documentRepository.findDocumentsByAsyncApiInfoAsyncApiState(AsyncApiState.PROCESSING);
    //        documentService.finDocumentsByAsyncApiState(AsyncApiState.PROCESSING);

    // find to_be_send -> to process not yet processed
    List<Document> waitingToSendDocs =
        documentRepository.findDocumentsByAsyncApiInfoAsyncApiState(AsyncApiState.WAITING_TO_SEND);
    //        documentService.finDocumentsByAsyncApiState(AsyncApiState.WAITING_TO_SEND);

    // TODO pick ration betweenThem
    List<Document> documentsWork = new ArrayList<>(completedDocs);
    documentsWork.addAll(processingDocs);
    documentsWork.addAll(waitingToSendDocs);
    documentsWork.addAll(cleaningDocs);

    // filtering documents already in process
    documentsWork =
        documentsWork.stream()
            .filter(document -> !virtualStorageService.isDocumentUsed(document.getId()))
            .collect(Collectors.toList());

    // add docs to virtual storage
    documentsWork.forEach(document -> virtualStorageService.addDocument(document.getId()));

    // pick subset of them with ratio to each group
    //    TODO every service/repo new instance??
    documentsWork.forEach(
        document1 ->
            taskExecutor.execute(
                new OcrApiJobWorker(
                    beanFactory.getBean(OCRService.class),
                    documentService,
                    documentRepository,
                    virtualStorageService,
                    document1)));

    log.info("Done executing works.");
  }

}
