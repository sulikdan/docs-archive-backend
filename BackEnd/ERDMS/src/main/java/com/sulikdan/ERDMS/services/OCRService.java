package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Document;

import java.util.List;

/**
 * Created by Daniel Šulik on 22-Jul-20
 * <p>
 * Class OCRService is service layer interface for communication with OCR API.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
public interface OCRService {

    Document extractTextFromDocument(Document document, DocConfig docConfig);

    Document extractTextFromDocument(Document document, String language, Boolean multiPageFile, Boolean highQuality);

    List<Document> extractTextFromDocuments(List<Document> document, String language, Boolean multiPageFile, Boolean highQuality);

    Document extractTextFromDocumentAsync(Document document, String language, Boolean multiPageFile, Boolean highQuality);

    List<Document> extractTextFromDocumentsAsync(List<Document> document, String language, Boolean multiPageFile, Boolean highQuality);
}
