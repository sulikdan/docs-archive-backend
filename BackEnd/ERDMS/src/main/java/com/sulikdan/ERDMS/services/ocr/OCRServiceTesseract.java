package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.statics.OcrRestApiSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Class OCRServiceTesseract is implementation of OCRService for tesseract API.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
// https://www.baeldung.com/spring-bean-scopes
// @Scope("prototype") TODO necessary??
@Slf4j
@Service
public class OCRServiceTesseract extends OcrRestApiSettings implements OCRService {

  DocumentRepository documentRepository;

  RestApiOcr restApiOcr;

  ObjectMapper mapper = new ObjectMapper();

  public OCRServiceTesseract(DocumentRepository documentRepository, RestApiOcr restApiOcr) {
    this.documentRepository = documentRepository;
    this.restApiOcr = restApiOcr;
  }

  @Override
  public Document extractTextFromDocument(Document document) {
    try {
      if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.WAITING_TO_SEND) {
        // TODO check what kind of file it is!
        // TODO Or set DocumentType Before??
        //        document.

        AsyncApiInfo result = restApiOcr.postDocumentRequest(document);
        if (result == null) return null;

        document.setAsyncApiInfo(result);

      } else if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.PROCESSING) {
        // Checking document status
        String statusUri =
            extractUriFromWholeURL(document.getAsyncApiInfo().getOcrApiDocStatus(), "ocr/");

        AsyncApiInfo asyncApiInfo = restApiOcr.getDocumentStatus(statusUri);
        if (asyncApiInfo == null) return null;

        document.setAsyncApiInfo(asyncApiInfo);

      } else if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.SCANNED) {
        //    Downloading scanned document
        String resultUri =
            extractUriFromWholeURL(document.getAsyncApiInfo().getOcrApiDocResult(), "ocr/");

        TessApiDoc resultDoc = restApiOcr.getDocumentResult(resultUri);
        if (resultDoc == null) return null;

        document.setPageList(
            resultDoc.getPages().stream().map(Page::new).collect(Collectors.toList()));
        document.getAsyncApiInfo().setAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

      } else if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.RESOURCE_TO_CLEAN) {
        //        deleting resources
        String resultUri =
            extractUriFromWholeURL(document.getAsyncApiInfo().getOcrApiDocResult(), "ocr/");

        if (restApiOcr.deleteDocument(resultUri)) {
          document.getAsyncApiInfo().setAsyncApiState(AsyncApiState.COMPLETED);
        }

      } else {
        throw new RuntimeException(
            "OCRService tesseract unexpected error!\n This shouldnt happen..");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return document;
  }

  /**
   * Returns location of resource without domain.
   *
   * @param url from which will be extracted
   * @param patternToSplit will be used to split and return second part.
   * @return
   */
  private String extractUriFromWholeURL(String url, String patternToSplit) {
    return url.split(patternToSplit)[1];
  }
}
