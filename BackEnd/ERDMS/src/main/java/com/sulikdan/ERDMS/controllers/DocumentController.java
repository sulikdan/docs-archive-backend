package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguage;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.FileStorageService;
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
@RestController
@RequestMapping("api/document")
public class DocumentController {

  protected final DocumentService documentService;
  protected final FileStorageService fileStorageService;

  ObjectMapper mapper = new ObjectMapper();

  public DocumentController(
      DocumentService documentService, FileStorageService fileStorageService) {
    this.documentService = documentService;
    this.fileStorageService = fileStorageService;
  }

  private static String generateUriForAsyncStatus(
      Path filePath, String methodName, String methodUri) {
    return MvcUriComponentsBuilder.fromController(DocumentController.class).toString()
        + methodUri
        + filePath.getFileName().toString();
  }

  /**
   * Generates name prefix for uploaded files. consisting of OCR_Timestamp
   *
   * @return strings "OCR_" + "current_timestamp_now()"
   * @implNote It's temporary solution and for many threaded usage, there may chance of collision
   *     and may need to be tweaked with adding thread number to it.
   */
  protected static String generateNamePrefix() {
    Date now = new Date();
    return "ERDMS_" + now.getTime();
  }

  /**
   * Simple check supported languages. There are also other languages, but 1st they need to be added
   * here and then make sure that correct tesseract dataset is in folder.
   *
   * @param language expecting string in lower-case
   */
  protected static void checkSupportedLanguages(String language) {
    switch (language) {
      case "eng":
      case "cz":
      case "svk":
        return;
      default:
        throw new UnsupportedLanguage(language);
    }
  }

  @ResponseBody
  @PostMapping(consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> uploadAndExtractTextSync(
      @RequestPart("files") MultipartFile[] files,
      @RequestParam(value = "lang", defaultValue = "eng") String lang,
      @RequestParam(value = "multiPageFile", defaultValue = "false") Boolean multiPageFile,
      @RequestParam(value = "highQuality", defaultValue = "false") Boolean highQuality)
      throws JsonProcessingException, IOException {

    // check language
    checkSupportedLanguages(lang);

    //        DocConfig docConfig = new DocConfig(highQuality, multiPageFile, lang, );

    // save file to disk + return Path
    List<Document> uploadedDocumentList = new ArrayList<>();
    for (MultipartFile file : files) {
      Path savedFilePath = fileStorageService.saveFile(file, generateNamePrefix());
      uploadedDocumentList.add(
          new Document(file.getName(), savedFilePath, ArrayUtils.toObject(file.getBytes()), null));
    }
    // createDoc with all provided params + add path + newID!!
    // send to DocumentSevice - layer

    // return result

    //        checkSupportedLanguages(lang);
    //
    //        List<Document> resultDocumentList = new ArrayList<>();
    //
    //        for (MultipartFile file : files) {
    //            Path savedFilePath = fileStorageService.saveFile(file, generateNamePrefix());
    //
    //            resultDocumentList.add(processFileSync(savedFilePath, file.getOriginalFilename(),
    // lang, multiPageFile, highQuality));
    //            fileStorageService.deleteFile(savedFilePath);
    //        }

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(""));
  }

  @GetMapping("/{documentId}")
  public ResponseEntity<String> getDocument(String documentId) throws JsonProcessingException {

    Document toReturn = documentService.findDocumentById(documentId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toReturn));
  }

  @GetMapping("/")
  public ResponseEntity<String> getDocuments() throws JsonProcessingException {
    List<Document> foundDocuments = documentService.findAllDocuments();
    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundDocuments));
  }
}
