package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Class OCRServiceTesseract is implementation of OCRService for tesseract API.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
@Service
public class OCRServiceTesseract implements OCRService {

    @Override
    public Document extractTextFromDocument(Document document, DocConfig docConfig) {
        return null;
    }

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
