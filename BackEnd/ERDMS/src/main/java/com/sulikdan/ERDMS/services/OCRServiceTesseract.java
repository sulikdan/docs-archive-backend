package com.sulikdan.ERDMS.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.sulikdan.ERDMS.entities.AsyncStatus;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.entities.DocumentType;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.statics.OcrRestApiSettings;
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
import java.nio.file.Files;
import java.util.List;

/**
 * Class OCRServiceTesseract is implementation of OCRService for tesseract API.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
// https://www.baeldung.com/spring-bean-scopes
// @Scope("prototype") TODO necessary??
@Service
public class OCRServiceTesseract extends OcrRestApiSettings implements OCRService {

  DocumentRepository documentRepository;

  public OCRServiceTesseract(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
  }

  @Override
  public Document extractTextFromDocument(Document document, DocConfig docConfig) {

    if (document.getAsyncDocument().getAsyncStatus() == AsyncStatus.WAITING_TO_SEND) {
      // TODO check what kind of file it is!
      // TODO Or set DocumentType Before??
      //        document.
      try {
        postDocumentRequest(document, docConfig);
      } catch (IOException e) {
        e.printStackTrace();
      }

    } else if (document.getAsyncDocument().getAsyncStatus() == AsyncStatus.PROCESSING) {

      getResultFromRequest(document);

    } else if (document.getAsyncDocument().getAsyncStatus() == AsyncStatus.COMPLETED) {
      throw new RuntimeException(
          "OCRService tesseract AsyncStatus completed - shouldnt be called!");
    } else {
      throw new RuntimeException("OCRService tesseract unexpected error!");
    }

    //    RestTemplate template = new RestTemplate();
    //    template.po

    return null;
  }

  public void postDocumentRequest(Document document, DocConfig docConfig) throws IOException {
    //    RestTemplate template = new RestTemplate();
    WebClient ocrClient = generatePostWebClient(document, docConfig);
    ResponseEntity<String> responseEntity = null;
    String header1 =
        String.format(
            "multipart/form-data; name=%s; filename=%s", "files", document.getNameOfFile());

    MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
    bodyBuilder
        .part("files", new ByteArrayResource(Files.readAllBytes(document.getFilePath())))
        .header(
            "Content-Disposition", "form-data; name=files; filename=" + document.getNameOfFile());
    addRequestTextParam(bodyBuilder,docConfig);

    if (document.getDocumentType() == DocumentType.IMG) {
      //      RequestHeadersSpec req1 =
      //              ocrClient.post().uri(IMG_DOC_CONTRL).bodyValue(document.getDocumentFile());
      //
      // ocrClient.post().uri(IMG_DOC_CONTRL).bodyValue(BodyInserters.fromMultipartData(bodyBuilder.build()))
      //              .exchange();
      //      req1.
      //      BodyInserters.fro
      ResponseEntity<JsonNode> jsonNode =
          ocrClient
              .post()
              .uri(IMG_DOC_CONTRL + "/test")
              .contentType(MediaType.MULTIPART_FORM_DATA)
              .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
              //
              // .bodyValue(BodyInserters.fromMultipartData(generateMultipartBody(document.getFilePath().toString())))
              .retrieve()
              .toEntity(JsonNode.class)
              .block();

      System.out.println("Received: ");
      System.out.println(jsonNode.getBody().toPrettyString());
      //              ocrClient.post().uri(IMG_DOC_CONTRL).body(BodyInserters.fromO);

      //      responseEntity = template.postForEntity(getImgUri(), );
    } else {
      ocrClient.post().uri(PDF_DOC_CONTRL);
      //      responseEntity = template.postForEntity(getPdfUri(),);
    }
    // Parse Response where to check and save it to document

    //    return document;
  }

  private MultiValueMap<String, HttpEntity<?>> generateMultipartBody(String filePath) {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    builder.part("file", new FileSystemResource(filePath));
    return builder.build();
  }

  protected Document getResultFromRequest(Document document) {

    //    if( document.getDocumentType() ==  DocumentType.IMG ){
    //      responseEntity = template.postForEntity(getImgUri(), );
    //    } else {
    //      responseEntity = template.postForEntity(getPdfUri(),);
    //    }
    // Parse Response of results
    // modify document
    return document;
  }

  protected WebClient generatePostWebClient(Document document, DocConfig docConfig) {
    return WebClient.builder()
        .baseUrl(BASE_URI)
        //            .defaultCookie("cookieKey", "cookieValue")
        //        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
        //        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultUriVariables(docConfig.getOcrPropertiesAsMap_X())
        //            .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080",
        // ))
        .build();
  }

  protected void addRequestTextParam(MultipartBodyBuilder builder, DocConfig docConfig) {
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

  protected WebClient generateGetWebClient(Document document, DocConfig docConfig) {
    return null;
  }

  // Very likely all are going to be unused!
  @Override
  public Document extractTextFromDocument(
      Document document, String language, Boolean multiPageFile, Boolean highQuality) {
    return null;
  }

  @Override
  public List<Document> extractTextFromDocuments(
      List<Document> document, String language, Boolean multiPageFile, Boolean highQuality) {
    return null;
  }

  @Override
  public Document extractTextFromDocumentAsync(
      Document document, String language, Boolean multiPageFile, Boolean highQuality) {
    return null;
  }

  @Override
  public List<Document> extractTextFromDocumentsAsync(
      List<Document> document, String language, Boolean multiPageFile, Boolean highQuality) {
    return null;
  }
}
