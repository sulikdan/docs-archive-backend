package com.sulikdan.ERDMS.bootstrap;

import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import com.sulikdan.ERDMS.services.DocumentService;
import com.sulikdan.ERDMS.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel Šulik on 22-Jul-20
 * <p>
 * Class DocumentBootstrap is used for .....
 */
@Slf4j
@Component
public class DocumentBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    protected final DocumentService documentService;
    protected final FileStorageService fileStorageService;

    protected final DocumentRepository documentRepository;

    public DocumentBootstrap(
            DocumentService documentService, FileStorageService fileStorageService, DocumentRepository documentRepository) {
        this.documentService    = documentService;
        this.fileStorageService = fileStorageService;
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.info("Loading documents.");

        List<Document> documents = loadDocuments();
        documentRepository.saveAll(documents);

        log.info("Documents should be loaded and stored");

    }

    private List<Document> loadDocuments(){

        Document document1 = Document.builder().id("11xyz11").nameOfFile("JustRandomFile1.jpg").documentFile(null).pageList(null).build();
        Document document2 = Document.builder().id("22xyz22").nameOfFile("JustRandomFile2.jpg").documentFile(null).pageList(null).build();
        Document document3 = Document.builder().id("33xyz33").nameOfFile("JustRandomFile3.jpg").documentFile(null).pageList(null).build();

        return Arrays.asList(document1,document2,document3);
    }
}
