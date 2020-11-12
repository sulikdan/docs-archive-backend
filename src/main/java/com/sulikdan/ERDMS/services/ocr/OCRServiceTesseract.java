package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.configurations.properties.OcrProperties;
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
@Slf4j
@Service
public class OCRServiceTesseract extends OcrRestApiSettings implements OCRService {

  private final DocRepository documentRepository;
  private final RestApiOcr restApiOcr;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String SPLIT_PATTERN = "/api/ocr";

  public OCRServiceTesseract(
      DocRepository documentRepository, RestApiOcr restApiOcr, OcrProperties ocrProperties) {
    super(ocrProperties);
    this.documentRepository = documentRepository;
    this.restApiOcr = restApiOcr;
  }

  @Override
  public Doc extractTextFromDoc(Doc doc) {
    try {
      if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.WAITING_TO_SEND
          || doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.MANUAL_SENDING) {
        // TODO check what kind of file it is!
        // TODO Or set DocumentType Before??
        //        document.

        AsyncApiInfo result = restApiOcr.postDocRequest(doc);
        if (result == null) return null;

        doc.setAsyncApiInfo(result);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.PROCESSING) {
        // Checking document status
        String statusUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocStatus());

        AsyncApiInfo asyncApiInfo = restApiOcr.getDocStatus(statusUri);
        if (asyncApiInfo == null) return null;

        doc.setAsyncApiInfo(asyncApiInfo);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.SCANNED) {
        //    Downloading scanned document
        String resultUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocResult());

        TessApiDoc resultDoc = restApiOcr.getDocResult(resultUri);
        if (resultDoc == null) {
          log.warn("Result doc was empty!!!");
          return null;
        }

        doc.setDocPageList(
            resultDoc.getPages().stream().map(DocPage::new).collect(Collectors.toList()));
        doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.RESOURCE_TO_CLEAN);

      } else if (doc.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.RESOURCE_TO_CLEAN) {
        //        deleting resources
        String resultUri =
            extractUriFromWholeURL(doc.getAsyncApiInfo().getOcrApiDocResult());

        if (restApiOcr.deleteDoc(resultUri)) {
          doc.getAsyncApiInfo().setAsyncApiState(AsyncApiState.COMPLETED);
        }

      } else {
        throw new RuntimeException(
            "OCRService tesseract unexpected error!\n This shouldnt happen..");
      }

    } catch (IOException e) {
      log.error("Received IO exception:" + e.getMessage());
      e.printStackTrace();
    }

    return doc;
  }

  /**
   * Returns location of resource without domain.
   *
   * @param url from which will be extracted
   * @return
   */
  private String extractUriFromWholeURL(String url) {
    log.info("Extracting URI from url: ->" + url + "<-");
    final String uriFromUrl = url.split(SPLIT_PATTERN)[1];
    log.debug("-->>||-->> Url before: " + url + " || after: " + uriFromUrl);
    return uriFromUrl;
  }
}
