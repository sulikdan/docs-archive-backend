package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguage;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel Šulik on 18-Jul-20
 *
 * <p>Class DocumentController is used for .....
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Slf4j
@RestController
@RequestMapping("api/document")
public class DocumentController {

  private final DocumentService documentService;

  ObjectMapper mapper = new ObjectMapper();

  public DocumentController(
      DocumentService documentService) {
    this.documentService = documentService;
  }

  private static String generateUriForAsyncStatus(
      Path filePath, String methodName, String methodUri) {
    return MvcUriComponentsBuilder.fromController(DocumentController.class).toString()
        + methodUri
        + filePath.getFileName().toString();
  }

  @ResponseBody
  @PostMapping(consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> uploadAndExtractTextSync(
      @RequestPart("files") MultipartFile[] files,
      @RequestParam(value = "lang", defaultValue = "eng") String lang,
      @RequestParam(value = "multiPageFile", defaultValue = "false") Boolean multiPageFile,
      @RequestParam(value = "highQuality", defaultValue = "false") Boolean highQuality,
      @RequestParam(value = "scanImmediately", defaultValue = "false") Boolean scanImmediately)
      throws JsonProcessingException, IOException {
    log.info("Getting file.");
    // check language
    checkSupportedLanguages(lang);

    // creating config with settings
    DocConfig docConfig = new DocConfig(highQuality, multiPageFile, lang, scanImmediately);

    //TODO change documents to something else(TDO), not to contain everything, just base stuff ...
    List<Document> uploadedDocumentList = documentService.processNewDocuments(files,docConfig);

    log.info("File processed: " + uploadedDocumentList.size());

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(uploadedDocumentList));
  }

  @GetMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDocument(String documentId) throws JsonProcessingException {

    Document toReturn = documentService.findDocumentById(documentId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toReturn));
  }

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDocuments() throws JsonProcessingException {
    List<Document> foundDocuments = documentService.findAllDocuments();
    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundDocuments));
  }

  /**
   * Simple check supported languages. There are also other languages, but 1st they need to be added
   * here and then make sure that correct tesseract dataset is in folder.
   *
   * @param language expecting string in lower-case
   */
  private static void checkSupportedLanguages(String language) {
    switch (language) {
      case "eng":
      case "cz":
      case "svk":
        return;
      default:
        throw new UnsupportedLanguage(language);
    }
  }
}
