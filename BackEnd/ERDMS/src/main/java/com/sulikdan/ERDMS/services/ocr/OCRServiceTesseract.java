package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.statics.OcrRestApiSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
  public Document extractTextFromDocument(Document document, DocConfig docConfig) {
    try {
      if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.WAITING_TO_SEND) {
        // TODO check what kind of file it is!
        // TODO Or set DocumentType Before??
        //        document.

        AsyncApiInfo result  = restApiOcr.postDocumentRequest(document);
        document.setAsyncApiInfo(result);

      } else if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.PROCESSING) {

        String statusUri =
            document
                .getAsyncApiInfo()
                .getOcrApiDocStatus()
                .substring(document.getAsyncApiInfo().getOcrApiDocStatus().indexOf("ocr/") + 3);
        // TODO save result to DB in case of Error
        AsyncApiInfo asyncApiInfo = restApiOcr.getDocumentStatus(statusUri);
        document.setAsyncApiInfo(asyncApiInfo);


      } else if (document.getAsyncApiInfo().getAsyncApiState() == AsyncApiState.COMPLETED) {
        // TODO call API for results!
        String resultUri =
                asyncApiInfo
                        .getOcrApiDocResult()
                        .substring(document.getAsyncApiInfo().getOcrApiDocResult().indexOf("ocr/") + 3);
        TessApiDoc resultDoc = restApiOcr.getDocumentResult(resultUri);
        document.setPageList(
                resultDoc.getPages().stream().map(Page::new).collect(Collectors.toList()));
        // TODO save to DB
        documentRepository.save(document);
        //TODO delete resource from OcrAPi after saving to DB
        //TODO fix string works!!!!
        restApiOcr.deleteDocument(resultUri);

      } else {
        throw new RuntimeException("OCRService tesseract unexpected error!");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }


  private MultiValueMap<String, HttpEntity<?>> generateMultipartBody(String filePath) {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    builder.part("file", new FileSystemResource(filePath));
    return builder.build();
  }


  /**
   * Returns location of resource without domain.
   * @param url from which will be extracted
   * @param patternToSplit will be used to split and return second part.
   * @return
   */
    private String extractUriFromWholeURL(String url, String patternToSplit){
      return  url.split(patternToSplit)[1];
    }
}
