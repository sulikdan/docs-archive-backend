package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import javax.print.Doc;

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
  private Document document;

  public OcrApiJobWorker(
          OCRService ocrService, DocumentService documentService, DocumentRepository documentRepository, Document document) {
    this.ocrService         = ocrService;
    this.documentService    = documentService;
    this.documentRepository = documentRepository;
    this.document = document;
  }

  @Override
  public void run() {

    log.info("Running OcrApiJobWorker.");

    Document returned = ocrService.extractTextFromDocument(document, document.getDocConfig());

    documentRepository.save(returned);

    log.info("Finished OcrApiJobWorker.");
  }
}
