package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.ocr.OCRServiceTesseract;
import com.sulikdan.ERDMS.services.ocr.RestApiOcr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by Daniel Šulik on 02-Aug-20
 *
 * <p>Class OCRServiceTesseractTest is used for .....
 */
class OCRServiceTesseractTest {

  @Mock
  DocRepository documentRepository;

  @Mock RestApiOcr restApiOcr;

  OCRServiceTesseract ocrService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    ocrService = new OCRServiceTesseract(documentRepository, restApiOcr);
  }

  @Test
  void testPostNewDocument() throws IOException {
    DocConfig config = new DocConfig(true, false, "eng", false);
    ClassPathResource resource = new ClassPathResource("OCR_TEXT_1.jpg");

    File file = resource.getFile();

    Doc doc =
        Doc.builder()
           .id("Yolooo")
           .asyncApiInfo(new AsyncApiInfo())
           .filePath(file.toPath())
           .nameOfFile(file.getName())
           .docType(DocType.IMG)
           .build();
    doc.setDocumentAsBytes(Files.readAllBytes(file.toPath()));
    //    TODO fix
    //    ocrService.postDocumentRequest(document,config);
  }

  @Test
  void testGetResultDoc() {
    DocConfig config = new DocConfig(true, false, "eng", false);
    Doc toRetrieve =
        Doc.builder()
           .id("cvasva")
           .asyncApiInfo(new AsyncApiInfo())
           .docType(DocType.IMG)
           .build();
    toRetrieve.getAsyncApiInfo().setAsyncApiState(AsyncApiState.PROCESSING);
    toRetrieve
        .getAsyncApiInfo()
        .setOcrApiDocStatus(
            "http://localhost:8080/ocr/document/OCR_1597007679261_OCR_TEXT_1.jpg/documentStatus");

    ocrService.extractTextFromDoc(toRetrieve);
  }
}
