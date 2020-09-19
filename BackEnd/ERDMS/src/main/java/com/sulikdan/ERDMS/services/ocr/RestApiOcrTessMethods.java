package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.services.statics.OcrRestApiSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Created by Daniel Šulik on 09-Aug-20
 *
 * <p>Class RestApiOcrMethods is used for .....
 */
@Slf4j
@Component
public class RestApiOcrTessMethods extends OcrRestApiSettings implements RestApiOcr {

  ObjectMapper mapper = new ObjectMapper();

  @Override
  public AsyncApiInfo postDocRequest(Doc doc)
      throws JsonProcessingException {

    WebClient ocrClient = generateWebClient(BASE_URI);
    ResponseEntity<JsonNode> jsonNode;
    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
    bodyBuilder
        .part("files", new ByteArrayResource(doc.getDocumentAsBytes()))
        .header(
            "Content-Disposition", "form-data; name=files; filename=" + doc.getNameOfFile());
    addRequestTextParam(bodyBuilder, doc.getDocConfig());

    if (doc.getDocType() == DocType.IMG) {
      jsonNode = executePostRequest(ocrClient, IMG_DOC_CONTRL, bodyBuilder);
    } else {
      jsonNode = executePostRequest(ocrClient, PDF_DOC_CONTRL, bodyBuilder);
    }
    // Parse Response where to check and save it to document
    //    AsyncDocInfo asyncDocInfoResult = null;
    //    if (!jsonNode.hasBody())
    if(jsonNode.getStatusCode().isError())
      return null;

    // parser + maybe it would be better to change approach expecting only 1 item?
    AsyncApiInfo asyncApiInfoResult =
        mapper
            .readValue(jsonNode.getBody().toString(), new TypeReference<List<AsyncApiInfo>>() {})
            .get(0);

    log.info("Received data from OCR-API:\n" + asyncApiInfoResult.toString());
    return asyncApiInfoResult;
  }

  @Override
  public AsyncApiInfo getDocStatus(String statusUri) throws JsonProcessingException {

    ResponseEntity<JsonNode> jsonNode = executeGetRequest(generateWebClient(BASE_URI), statusUri);

    if(jsonNode.getStatusCode().isError())
      return null;

    AsyncApiInfo requestResult =
        mapper.readValue(jsonNode.getBody().toString(), AsyncApiInfo.class);

    log.info("GetStatus data from OCR-API:\n" + requestResult.toString());
    return requestResult;
  }

  @Override
  public TessApiDoc getDocResult(String resultUri) throws JsonProcessingException {
    ResponseEntity<JsonNode> jsonNode = executeGetRequest(generateWebClient(BASE_URI), resultUri);

    if(jsonNode.getStatusCode().isError())
      return null;

    TessApiDoc requestResult = mapper.readValue(jsonNode.getBody().toString(), TessApiDoc.class);

    log.info("Get data from OCR-API:\n" + requestResult.toString());
    return requestResult;
  }

  @Override
  public boolean deleteDoc(String deleteUri) {
    ResponseEntity<JsonNode> jsonNode =
        executeDeleteRequest(generateWebClient(BASE_URI), deleteUri);
    return jsonNode.getStatusCode().is2xxSuccessful()
        || jsonNode.getStatusCode().equals(HttpStatus.NOT_FOUND);
  }

  private ResponseEntity<JsonNode> executePostRequest(
      WebClient ocrClient, String reqUri, MultipartBodyBuilder bodyBuilder) {
    return ocrClient
        .post()
        .uri(reqUri)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
        .retrieve()
        .toEntity(JsonNode.class)
        .block();
  }

  private ResponseEntity<JsonNode> executeGetRequest(WebClient ocrClient, String reqUri) {
    return ocrClient.get().uri(reqUri).retrieve().toEntity(JsonNode.class).block();
  }

  private ResponseEntity<JsonNode> executeDeleteRequest(WebClient ocrClient, String delUri) {
    return ocrClient.delete().uri(delUri).retrieve().toEntity(JsonNode.class).block();
  }

  private WebClient generateWebClient(String baseUri) {
    return WebClient.builder().baseUrl(baseUri).build();
  }

  private void addRequestTextParam(MultipartBodyBuilder builder, DocConfig docConfig) {
    // TODO pass whole config as json object and not as every param seperately ...
    builder
        .part("lang", docConfig.getLang().toString(), MediaType.TEXT_PLAIN)
        .header("Content-Disposition", "form" + "-data; " + "name=lang")
        .header("Content-type", "text/plain");
    builder
        .part("multiPageFile", docConfig.getMultiPage().toString(), MediaType.TEXT_PLAIN)
        .header("Content-Disposition", "form" + "-data; " + "name=multiPageFile")
        .header("Content-type", "text/plain");
    builder
        .part("highQuality", docConfig.getHighQuality().toString(), MediaType.TEXT_PLAIN)
        .header("Content-Disposition", "form" + "-data; " + "name=highQuality")
        .header("Content-type", "text/plain");
  }
}
