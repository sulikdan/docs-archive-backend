package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguage;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Šulik on 18-Jul-20
 * <p>
 * Class DocumentController is used for .....
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

    public DocumentController(DocumentService documentService, FileStorageService fileStorageService) {
        this.documentService    = documentService;
        this.fileStorageService = fileStorageService;
    }

    @ResponseBody
    @PostMapping(consumes = "multipart/form-data",
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadAndExtractTextSync(
            @RequestPart("files") MultipartFile[] files, @RequestParam(value = "lang",
                                                                       defaultValue = "eng") String lang,
            @RequestParam(value = "multiPageFile",
                          defaultValue = "false") Boolean multiPageFile, @RequestParam(value = "highQuality",
                                                                                       defaultValue = "false")
                    Boolean highQuality) throws JsonProcessingException {

        // check language
        checkSupportedLanguages(lang);

        // save file to disk + return Path
        List<Document> resultDocumentList = new ArrayList<>();
        for (MultipartFile multipartFile : files){
            Path savedFilePath = fileStorageService.saveFile(multipartFile, generateNamePrefix());

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
        //            resultDocumentList.add(processFileSync(savedFilePath, file.getOriginalFilename(), lang, multiPageFile, highQuality));
        //            fileStorageService.deleteFile(savedFilePath);
        //        }

        return ResponseEntity.status(HttpStatus.OK).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(""));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<String> getDocument(String documentId) throws JsonProcessingException {

        Document toReturn = documentService.findDocumentById(documentId);

        return ResponseEntity.status(HttpStatus.OK).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(toReturn));
    }

    @GetMapping("/")
    public ResponseEntity<String> getDocuments() throws JsonProcessingException {
        List<Document> foundDocuments = documentService.findAllDocuments();
        return ResponseEntity.status(HttpStatus.OK).body(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(foundDocuments));
    }

    // TODO delete after refactoring!!
    private Document processFileSync(Path filePath, String lang, Boolean highQuality) throws JsonProcessingException {
        //        return ocrService.extractTextFromFile(null, "filePath", lang, false, highQuality);
        return null;
    }

    private Document processFileSync(
            Path savedFilePath, String origFileName, String lang, Boolean multipageTiff, Boolean highQuality) {
        //        TODO
        //        return ocrService.extractTextFromFile(savedFilePath, origFileName, lang, multipageTiff, highQuality);
        return null;
    }

    private static String generateUriForAsyncStatus(
            Path filePath, String methodName, String methodUri) {
        return MvcUriComponentsBuilder.fromController(DocumentController.class).toString() + methodUri + filePath.getFileName().toString();
    }

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


}