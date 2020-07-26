package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.entities.DocConfig;

import java.util.List;

/**
 * <p>
 * Class DocumentService is service layer interface to work with documents.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
public interface DocumentService {

    /**
     * A method to find a document using provided id.
     * @param id identificator of document
     * @return the searched document or NotFoundException
     */
    Document findDocumentById(String id);

    /**
     * A method to find/get all documents.
     * @return List of Documents
     */
    List<Document> findAllDocuments();


    /**
     * Calls many other methods to create new Record in DB and also sends it to be processed
     * by OCR sooner or later.
     * @param document to be saved & processed
     * @param docConfig settings for OCR, how to scan the document
     * @return the saved Document with assigned ID and details
     */
    Document createNewDocument(Document document, DocConfig docConfig);

    /**
     * Saves document using repo layer.
     * @param document object to be saved
     * @return the saved document
     */
    Document saveDocument(Document document);

    /**
     * Updates document using repo layer.
     * @param document object to be updated using provided Id in document
     */
    void updateDocument(Document document);


}
