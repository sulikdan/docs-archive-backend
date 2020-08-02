package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Daniel Šulik on 25-Jul-20
 * <p>
 * Class DocumentServiceImplTest is used for JUnit tests of DocumentServiceImpl of all interface methods implemented
 * from DocumentService.
 */
class DocumentServiceImplTest {

    @Mock
    DocumentRepository documentRepository;

    DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

//        documentService = new DocumentServiceImpl(ocrService, virtualStorageService, fileStorageService,
//                                                  documentRepository);
    }

    @Test
    void findDocumentById() {
        Document toBeFind = Document.builder().id("abcd").build();
        Optional<Document> optionalDocument = Optional.of(toBeFind);

        when(documentRepository.findById(anyString())).thenReturn(optionalDocument);

        Document documentFound = documentService.findDocumentById("abcd");

        Assert.assertNotNull("Null document returned!",documentFound);
        Assert.assertEquals(toBeFind.getId(),documentFound.getId());
        verify(documentRepository, times(1)).findById(anyString());
        verify(documentRepository, never()).findAll();
    }

    @Test
    void findDocumentByIdNotFound() throws Exception {
        when(documentRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            documentService.findDocumentById("abcd");
        });
        verify(documentRepository, times(1)).findById(anyString());
        verify(documentRepository, never()).findAll();
    }

    @Test
    void findAllDocuments() {
        Document toBeFind1 = Document.builder().id("abcd").build();
        Document toBeFind2 = Document.builder().id("abc").build();
        List<Document> documentList = Arrays.asList(toBeFind1, toBeFind2);

        when(documentRepository.findAll()).thenReturn(documentList);

        List<Document> foundList = documentService.findAllDocuments();

        Assert.assertNotNull(foundList);
        Assert.assertEquals(2,foundList.size
                ());
        verify(documentRepository, times(1)).findAll();
        verify(documentRepository, never()).findById(anyString());

    }

    @Ignore
    @Test
    void saveDocument() {
    }

    @Ignore
    @Test
    void updateDocument() {
    }
}