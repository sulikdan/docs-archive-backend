package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.sulikdan.ERDMS.dto.DocDto;
import com.sulikdan.ERDMS.dto.DocDtoConverter;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguage;
import com.sulikdan.ERDMS.services.DocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Created by Daniel Šulik on 18-Jul-20
 *
 * <p>Class DocumentController is used for API accessing from outside to documents stored inside of
 * appication.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200/import", "http://localhost:4200"})
@RequestMapping("api/documents")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class DocController {

  private final DocService docService;
  private final DocDtoConverter docDtoConverter;

  ObjectMapper mapper;

  public DocController(DocService docService, DocDtoConverter docDtoConverter) {
    this.docService      = docService;
    this.docDtoConverter = docDtoConverter;
    this.mapper          = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  private static String generateUriForAsyncStatus(
      Path filePath, String methodName, String methodUri) {
    return MvcUriComponentsBuilder.fromController(DocController.class).toString()
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

    log.info("Amount of files: " + String.valueOf(files.length));
    // check language
    checkSupportedLanguages(lang);

    // creating config with settings
    DocConfig docConfig = new DocConfig(highQuality, multiPageFile, lang, scanImmediately);

    log.info("Doc settigns: " + docConfig.toString());

    // TODO change documents to something else(TDO), not to contain everything, just base stuff ...
    List<Doc> uploadedDocList = docService.processNewDocs(files, docConfig);

    log.info("File processed: " + uploadedDocList.size());

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(uploadedDocList));
  }

  @GetMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDoc(String documentId) throws JsonProcessingException {

    Doc toReturn = docService.findDocById(documentId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toReturn));
  }

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDocs(
      UriComponentsBuilder builder,
      SearchDocParams searchDocParams,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "25") int size)
      throws JsonProcessingException {

    if (searchDocParams == null) searchDocParams = new SearchDocParams();

    //    TODO 2. catch errows?

    List<Doc> foundDocs = docService.findDocsUsingSearchParams(searchDocParams, page, size);
    List<DocDto> docDtos = new ArrayList<>();

    for (Doc doc : foundDocs) {
      DocDto docDto = docDtoConverter.convertToDto(doc);
      Link selfLink = linkTo(DocController.class).slash(docDto.getId()).withSelfRel();
      docDto.add(selfLink);
      docDtos.add(docDto);
    }
//
    Link link = linkTo(DocController.class).withSelfRel();
    CollectionModel<DocDto> docs = CollectionModel.of(docDtos, link);

//    return ResponseEntity.status(HttpStatus.OK)
//        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundDocs));
    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(docs));
  }

  @PatchMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void update(@PathVariable("id") final Long id, @RequestBody final Doc resource) {
    Preconditions.checkNotNull(resource);
    //    TODO
    //    RestPreconditions.checkFound(service.findById(resource.getId()));
    //    service.update(resource);
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
