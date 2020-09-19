package com.sulikdan.ERDMS.services;

import com.querydsl.core.types.Predicate;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.repositories.mongo.DocCustomRepository;
import com.sulikdan.ERDMS.services.ocr.OCRService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Daniel Šulik on 25-Jul-20
 *
 * <p>Class DocumentServiceImplTest is used for JUnit tests of DocumentServiceImpl of all interface
 * methods implemented from DocumentService.
 */
class DocServiceImplTest {

  @Mock DocRepository documentRepository;

  @Mock DocCustomRepository customRepository;

  @Mock TaskExecutor taskExecutor;

  @Mock OCRService ocrService;

  @Mock VirtualStorageService virtualStorageService;

  @Mock FileStorageService storageService;

  DocService docService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    docService =
        new DocServiceImpl(
            taskExecutor, ocrService, virtualStorageService, storageService, documentRepository);
  }

  @Test
  void findDocumentById() {
    Doc toBeFind = Doc.builder().id("abcd").build();
    Optional<Doc> optionalDocument = Optional.of(toBeFind);

    when(documentRepository.findById(anyString())).thenReturn(optionalDocument);

    Doc docFound = docService.findDocById("abcd");

    Assert.assertNotNull("Null document returned!", docFound);
    Assert.assertEquals(toBeFind.getId(), docFound.getId());
    verify(documentRepository, times(1)).findById(anyString());
    verify(documentRepository, never()).findAll();
  }

  @Test
  void findDocumentByIdNotFound() throws Exception {
    when(documentRepository.findById(anyString())).thenReturn(Optional.empty());

    Assertions.assertThrows(
        RuntimeException.class,
        () -> {
          docService.findDocById("abcd");
        });
    verify(documentRepository, times(1)).findById(anyString());
    verify(documentRepository, never()).findAll();
  }

  @Test
  void findAllDocuments() {
    Doc toBeFind1 = Doc.builder().id("abcd").build();
    Doc toBeFind2 = Doc.builder().id("abc").build();
    List<Doc> docList = Arrays.asList(toBeFind1, toBeFind2);

    when(documentRepository.findAll()).thenReturn(docList);

    List<Doc> foundList = docService.findAllDocs();

    Assert.assertNotNull(foundList);
    Assert.assertEquals(2, foundList.size());
    verify(documentRepository, times(1)).findAll();
    verify(documentRepository, never()).findById(anyString());

    //        documentMongoRepository.fi

  }

  @Test
  void findDocsUsingSearchParams() {
    SearchDocParams docParams = new SearchDocParams();
    docParams.getLanguages().add("eng");

    Doc toBeFind1 =
        Doc.builder().id("abcd").docConfig(new DocConfig(false, false, "eng", false)).build();
    List<Doc> docs = Collections.singletonList(toBeFind1);
    //    Doc toBeFind2 = Doc.builder().id("xyz").docConfig(new DocConfig(false, false,"czk",
    // false)).build();

    when(documentRepository.findDocsByMultipleArgs(docParams, 1, 5)).thenReturn(docs);

    List<Doc> foundList = docService.findDocsUsingSearchParams(docParams, 1, 5);

    Assert.assertNotNull(foundList);
    Assert.assertEquals(1, foundList.size());
    verify(documentRepository, times(1)).findDocsByMultipleArgs(docParams, 1, 5);
    verify(documentRepository, never()).findAll();
  }

  @Ignore
  @Test
  void saveDocument() {}

  @Ignore
  @Test
  void updateDocument() {}
}
