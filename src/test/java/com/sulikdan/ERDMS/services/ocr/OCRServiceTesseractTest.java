package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.configurations.properties.OcrProperties;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.statics.OcrRestApiSettings;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class OCRServiceTesseractTest {

  @Mock DocRepository documentRepository;
  @Mock RestApiOcr restApiOcr;
  @Mock OcrProperties ocrProperties;

  ObjectMapper mapper = new ObjectMapper();
  Doc doc = null;

  String SPLIT_PATTERN = "/api/ocr";

  OCRServiceTesseract ocrServiceTesseract;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    doc = new Doc();
    doc.setDocConfig(new DocConfig(false,false,"eng",false));
    doc.setAsyncApiInfo(new AsyncApiInfo());
    doc.getAsyncApiInfo().setOcrApiDocStatus("localhost:8080/api/ocr/documents/blablbalba");
    doc.getAsyncApiInfo().setOcrApiDocResult("localhost:8080/api/ocr/documents/blablbalba");

    ocrServiceTesseract = new OCRServiceTesseract(documentRepository, restApiOcr, ocrProperties);
  }

  @Test
  void extractTextFromDocSending() throws JsonProcessingException {
    //Given
    doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.WAITING_TO_SEND);

    AsyncApiInfo toBeResult = new AsyncApiInfo();
    toBeResult.setAsyncApiState(AsyncApiState.PROCESSING);

    when(restApiOcr.postDocRequest(any(Doc.class))).thenReturn(toBeResult);


    //when
    Doc result = ocrServiceTesseract.extractTextFromDoc(doc);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(toBeResult.getAsyncApiState(), result.getAsyncApiInfo().getAsyncApiState());

  }


  @Test
  void extractTextFromDocProcessing() throws JsonProcessingException {
    //Given
    doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.PROCESSING);

    AsyncApiInfo toBeResult = new AsyncApiInfo();
    toBeResult.setAsyncApiState(AsyncApiState.SCANNED);

    when(restApiOcr.getDocStatus(anyString())).thenReturn(toBeResult);


    //when
    Doc result = ocrServiceTesseract.extractTextFromDoc(doc);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(toBeResult.getAsyncApiState(), result.getAsyncApiInfo().getAsyncApiState());

  }


  @Test
  void extractTextFromDocScanned() throws JsonProcessingException {
    //Given
    doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.SCANNED);

    AsyncApiInfo toBeResult = new AsyncApiInfo();
    toBeResult.setAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

    TessApiDoc tessApiDoc = new TessApiDoc();
    tessApiDoc.setPages(new ArrayList<>());

    when(restApiOcr.getDocResult(anyString())).thenReturn(tessApiDoc);


    //when
    Doc result = ocrServiceTesseract.extractTextFromDoc(doc);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(toBeResult.getAsyncApiState(), result.getAsyncApiInfo().getAsyncApiState());

  }


  @Test
  void extractTextFromDocResourceToManualSending() throws JsonProcessingException {
    //Given
    doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

    AsyncApiInfo toBeResult = new AsyncApiInfo();
    toBeResult.setAsyncApiState(AsyncApiState.COMPLETED);

    when(restApiOcr.deleteDoc(anyString())).thenReturn(true);


    //when
    Doc result = ocrServiceTesseract.extractTextFromDoc(doc);

    //then
    Assert.assertNotNull(result);
    Assert.assertEquals(toBeResult.getAsyncApiState(), result.getAsyncApiInfo().getAsyncApiState());

  }

}
