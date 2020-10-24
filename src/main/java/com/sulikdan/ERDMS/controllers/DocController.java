package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.sulikdan.ERDMS.configurations.configs.JwtTokenUtil;
import com.sulikdan.ERDMS.dto.DocDto;
import com.sulikdan.ERDMS.dto.DocDtoConverter;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguageException;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
@CrossOrigin(
    origins = {
      "http://localhost:4200/import",
      "http://localhost:4200",
      "http://localhost:4200/documents"
    })
@RequestMapping("api/documents")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class DocController {

  private final DocService docService;
  private final DocDtoConverter docDtoConverter;
  private final UserService userService;
  private final JwtTokenUtil jwtTokenUtil;
  private final ModelMapper modelMappper;
  private final ObjectMapper mapper;

  public DocController(
      DocService docService,
      DocDtoConverter docDtoConverter,
      UserService userService,
      JwtTokenUtil jwtTokenUtil) {
    this.docService = docService;
    this.docDtoConverter = docDtoConverter;
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.modelMappper = new ModelMapper();
    this.mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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

    User user = loadConnectedUser();

    // check language
    checkSupportedLanguages(lang);

    // creating config with settings
    DocConfig docConfig = new DocConfig(highQuality, multiPageFile, lang, scanImmediately);

    log.info("Doc settigns: " + docConfig.toString());

    List<Doc> uploadedDocList = docService.processNewDocs(files, docConfig, user);

    List<DocDto> foundDocDtos = convertDocToDocDtoWithLinks(uploadedDocList);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundDocDtos));
  }

  @GetMapping(value = "/{documentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDoc(@PathVariable String documentId)
      throws JsonProcessingException {

    User user = loadConnectedUser();

    Doc toReturn = docService.findDocById(documentId, user);

    if (toReturn != null) {

      DocDto toReturnDocDto = docDtoConverter.convertToDto(toReturn);
      Link selfLink = linkTo(DocController.class).slash(toReturnDocDto.getId()).withSelfRel();
      toReturnDocDto.add(selfLink);

      return ResponseEntity.status(HttpStatus.OK)
          .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toReturnDocDto));

    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(""));
    }
  }

  @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDocs(String searchDocParams) throws JsonProcessingException {

    if (searchDocParams == null) {
      log.info("SearchDocParam is empty!");
    } else {
      log.info("SearchDocParams length: " + searchDocParams.length());
    }

    SearchDocParams convertedParams = null;
    try {
      convertedParams = mapper.readValue(searchDocParams, SearchDocParams.class);
    } catch (Exception e) {
      System.out.println("it fucked..");
      System.out.println(e.getMessage());
    }

    log.info("Search doc params input:" + searchDocParams.toString());
    log.info("Search doc params mapped:" + convertedParams.toString());

    User user = loadConnectedUser();
    Page<Doc> pagedDocs;
    if (convertedParams != null
        && convertedParams.getPageIndex() != null
        && convertedParams.getPageSize() != null) {
      pagedDocs =
          docService.findDocsUsingSearchParams(
              convertedParams, convertedParams.getPageIndex(), convertedParams.getPageSize(), user);
    } else {
      log.warn("Called default doc search.");
      pagedDocs = docService.findDocsUsingSearchParams(convertedParams, 0, 20, user);
    }

    Page<DocDto> pagedDocDtos = convertDocToDocDtoWithLinks(pagedDocs);

    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pagedDocDtos));
  }

  @GetMapping(value = "/{documentId}/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getDocFile(@PathVariable String documentId)
      throws JsonProcessingException {

    User user = loadConnectedUser();

    Doc foundDoc = docService.findDocById(documentId, user);

    if (foundDoc != null) {
      return ResponseEntity.status(HttpStatus.OK)
          .header(
              "Content-Disposition", "attachment; filename=\"" + foundDoc.getNameOfFile() + "\"")
          .body(foundDoc.getDocumentAsBytes());
      //      return foundDoc.getDocumentAsBytes();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<String> update(
      @PathVariable("id") final String id, @RequestBody String updateDoc)
      throws JsonProcessingException {
    log.info("Patching.." + updateDoc);
    Preconditions.checkNotNull(updateDoc);

    DocDto docDtoResource = null;
    try {
      docDtoResource = mapper.readValue(updateDoc, DocDto.class);
    } catch (Exception e) {
      log.info("It's distash!!");
      log.info(e.getMessage());
    }

    log.info("DocDto resource:" + docDtoResource.toString());

    log.info("Updating..");
    Doc docResourece = docDtoConverter.convertToEntity(docDtoResource);
    log.info("Converted: " + docResourece.toString());

    User user = loadConnectedUser();

    docService.updateDocument(docResourece, user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString("OK"));
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteDocument(@PathVariable("id") final String id) {
    log.info("Deleting document:" + id);

    User user = loadConnectedUser();
    docService.deleteDocumentById(id, user);
  }

  private Page<DocDto> convertDocToDocDtoWithLinks(Page<Doc> pagedDocList) {

    List<DocDto> docDtos = new ArrayList<>();

    for (Doc doc : pagedDocList.getContent()) {
      docToDocDtoInList(docDtos, doc);
    }

    return new PageImpl<>(docDtos, pagedDocList.getPageable(), pagedDocList.getTotalElements());
  }

  private List<DocDto> convertDocToDocDtoWithLinks(List<Doc> docList) {

    List<DocDto> docDtos = new ArrayList<>();

    for (Doc doc : docList) {
      docToDocDtoInList(docDtos, doc);
    }

    return docDtos;
  }

  private void docToDocDtoInList(List<DocDto> docDtos, Doc doc) {
    DocDto docDto = docDtoConverter.convertToDto(doc);
    Link selfLink = linkTo(DocController.class).slash(docDto.getId()).withSelfRel();
    Link fileLink = linkTo(DocController.class).slash(docDto.getId()).slash("file").withRel("file");
    docDto.add(selfLink);
    docDto.add(fileLink);
    docDtos.add(docDto);
  }

  private User loadConnectedUser() {
    final String username = SecurityContextHolder.getContext().getAuthentication().getName();
    final Optional<User> userLoaded = userService.loadUserByUserName(username);
    if (!userLoaded.isPresent()) throw new RuntimeException("User not found!");
    final User user = userLoaded.get();
    log.info("Found what? :: " + (user.toString()));
    return user;
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
        throw new UnsupportedLanguageException(language);
    }
  }
}
