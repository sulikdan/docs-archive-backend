package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.repositories.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Class DocumentServiceImpl is implementation of DocumentService interface.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    // services
    protected final OCRService ocrService;
    protected final VirtualStorageService virtualStorageService;

    // repos
    protected final DocumentRepository documentRepository;


    public DocumentServiceImpl(
            OCRService ocrService, VirtualStorageService virtualStorageService, DocumentRepository documentRepository) {
        this.ocrService            = ocrService;
        this.virtualStorageService = virtualStorageService;
        this.documentRepository    = documentRepository;
    }

    @Override
    public Document findDocumentById(String id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if( !optionalDocument.isPresent() ){
            log.warn(MessageFormat.format("Document with id={} not found!",id));
            //TODO
            throw new RuntimeException("Not found");
        }

        return optionalDocument.get();
    }

    @Override
    public List<Document> findAllDocuments() {
        List<Document> documentList = new ArrayList<>();
        documentRepository.findAll().forEach(documentList::add);
        return documentList;
    }

    @Override
    public Document createNewDocument(Document document, DocConfig docConfig) {

        if( docConfig.getScanImmediately() ){
            // send request to OCR-API
            Document sentToOCR = ocrService.extractTextFromDocument(document, docConfig);
            // save results to queue & to be later processed
            virtualStorageService.addDocument(sentToOCR);
            // save to DB
            saveDocument(sentToOCR);
            // return result
            return sentToOCR;
        } else {

            // save to DB
            saveDocument(document);
            // return result
            return document;
        }
    }

    @Override
    public Document saveDocument(Document document) {
        document.setId(new ObjectId().toString());
        return documentRepository.save(document);
    }

    @Override
    public void updateDocument(Document document) {
//      TODO it's not best impl... should check saving and overriding of element values ..
        documentRepository.save(document);
    }
}
