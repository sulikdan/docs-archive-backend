package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;

import java.util.List;

/**
 * Created by Daniel Šulik on 22-Jul-20
 * <p>
 * Class OCRServiceTesseract is used for .....
 */
public class OCRServiceTesseract implements OCRService {

    @Override
    public Document extractTextFromDocument(
            Document document, String language, Boolean multiPageFile, Boolean highQuality) {
        return null;
    }


    @Override
    public List<Document> extractTextFromDocuments(
            List<Document> document, String language, Boolean multiPageFile, Boolean highQuality) {
        return null;
    }

    @Override
    public Document extractTextFromDocumentAsync(Document document, String language, Boolean multiPageFile, Boolean highQuality) {
        return null;
    }

    @Override
    public List<Document> extractTextFromDocumentsAsync(
            List<Document> document, String language, Boolean multiPageFile, Boolean highQuality) {
        return null;
    }
}
