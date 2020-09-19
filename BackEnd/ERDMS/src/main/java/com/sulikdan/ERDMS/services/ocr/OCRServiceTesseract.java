package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocRepository;
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

  private final DocRepository documentRepository;
  private final RestApiOcr restApiOcr;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String SPLIT_PATTERN = "/ocr";


  public OCRServiceTesseract(DocRepository documentRepository, RestApiOcr restApiOcr) {
    this.documentRepository = documentRepository;
    this.restApiOcr = restApiOcr;
  }

  @Override
  public Doc extractTextFromDoc(Doc doc) {
    try {
      if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.WAITING_TO_SEND || doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.MANUAL_SENDING) {
        // TODO check what kind of file it is!
        // TODO Or set DocumentType Before??
        //        document.

        AsyncApiInfo result = restApiOcr.postDocRequest(doc);
        if (result == null) return null;

        doc.setAsyncApiInfo(result);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.PROCESSING) {
        // Checking document status
        String statusUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocStatus(), SPLIT_PATTERN);

        AsyncApiInfo asyncApiInfo = restApiOcr.getDocStatus(statusUri);
        if (asyncApiInfo == null) return null;

        doc.setAsyncApiInfo(asyncApiInfo);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.SCANNED) {
        //    Downloading scanned document
        String resultUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocResult(), SPLIT_PATTERN);

        TessApiDoc resultDoc = restApiOcr.getDocResult(resultUri);
        if (resultDoc == null) return null;

        doc.setPageList(
            resultDoc.getPages().stream().map(Page::new).collect(Collectors.toList()));
        doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.RESOURCE_TO_CLEAN) {
        //        deleting resources
        String resultUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocResult(), SPLIT_PATTERN);

        if (restApiOcr.deleteDoc(resultUri)) {
          doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.COMPLETED);
        }

      } else {
        throw new RuntimeException(
            "OCRService tesseract unexpected error!\n This shouldnt happen..");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return doc;
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
