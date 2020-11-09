package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.VirtualStorageService;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.text.MessageFormat;
import java.util.Arrays;

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
      OCRService ocrService,
      DocService docService,
      DocRepository documentRepository,
      VirtualStorageService virtualStorageService,
      Doc doc) {
    this.ocrService = ocrService;
    this.docService = docService;
    this.documentRepository = documentRepository;
    this.virtualStorageService = virtualStorageService;
    this.doc = doc;
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

      try {
        returned = ocrService.extractTextFromDoc(doc);
      } catch (Exception e) {
        log.error(
            MessageFormat.format(
                "There was issue with OCR scanning document {0}.\n With error message: {1}.",
                returned.getId(), e.getMessage()));
        log.error(Arrays.toString(e.getStackTrace()));

        e.printStackTrace();

        // remove from map containing docs in use
        virtualStorageService.deleteDoc(returned.getId());

        if( virtualStorageService.docFailedTimes(returned.getId()) >= 3 ){
          returned.getAsyncApiInfo().setAsyncApiState(AsyncApiState.FAILED);
          docService.saveDoc(returned);
          continue;
        }

        virtualStorageService.addOrIncreaseFailedDoc(returned.getId());

      }

      if (returned != null) docService.saveDoc(returned);
      if (whileCounter++ >= 10) {
        // TODO in case there is exception the doc is not deleted rom virtualStorage!!
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
