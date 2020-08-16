package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.VirtualStorageService;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by Daniel Šulik on 25-Jul-20
 *
 * <p>Class OcrApiJobWorker is used for .....
 */
@Slf4j
@Async("threadPoolTaskExecutor")
public class OcrApiJobWorker implements Runnable {

  private OCRService ocrService;
  private DocumentService documentService;
  private DocumentRepository documentRepository;
  private VirtualStorageService virtualStorageService;
  private Document document;

  public OcrApiJobWorker(
          OCRService ocrService, DocumentService documentService, DocumentRepository documentRepository,
          VirtualStorageService virtualStorageService, Document document) {
    this.ocrService            = ocrService;
    this.documentService       = documentService;
    this.documentRepository    = documentRepository;
    this.virtualStorageService = virtualStorageService;
    this.document              = document;
  }

  @Override
  public void run() {

    log.info("Running OcrApiJobWorker.");

    AsyncApiState lastState = document.getAsyncApiInfo().getAsyncApiState();
    Document returned = document;

    // reapeat communication till there is change and its succesfull
    do {
      lastState = document.getAsyncApiInfo().getAsyncApiState();
      returned = ocrService.extractTextFromDocument(document);

      if (returned != null) documentService.saveDocument(returned);

    } while (returned != null
        && returned.getAsyncApiInfo().getAsyncApiState() != lastState
        && returned.getAsyncApiInfo().getAsyncApiState() != AsyncApiState.COMPLETED);

    virtualStorageService.deleteDocument(document.getId());

    log.info("Finished OcrApiJobWorker.");
  }
}
