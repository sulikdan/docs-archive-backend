package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.DocService;
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
  private DocService docService;
  private DocRepository documentRepository;
  private VirtualStorageService virtualStorageService;
  private Doc doc;

  public OcrApiJobWorker(
          OCRService ocrService, DocService docService, DocRepository documentRepository,
          VirtualStorageService virtualStorageService, Doc doc) {
    this.ocrService         = ocrService;
    this.docService         = docService;
    this.documentRepository = documentRepository;
      this.virtualStorageService = virtualStorageService;
      this.doc                   = doc;
  }

  @Override
  public void run() {

    log.info("Running OcrApiJobWorker.");

    AsyncApiState lastState = doc.getAsyncApiInfo().getAsyncApiState();
    Doc returned = doc;

    int whileCounter = 0;
    // reapeat communication till there is change and its succesfull
    do {
      lastState = doc.getAsyncApiInfo().getAsyncApiState();
      returned = ocrService.extractTextFromDoc(doc);

      if (returned != null) docService.saveDoc(returned);
      if ( whileCounter++ >= 10 ){
        log.error("Counter reached more than it should!");
        break;
      }

    } while (returned != null
        && returned.getAsyncApiInfo().getAsyncApiState() != lastState
        && returned.getAsyncApiInfo().getAsyncApiState() != AsyncApiState.COMPLETED);

    virtualStorageService.deleteDoc(doc.getId());

    log.info("Finished OcrApiJobWorker.");
  }
}
