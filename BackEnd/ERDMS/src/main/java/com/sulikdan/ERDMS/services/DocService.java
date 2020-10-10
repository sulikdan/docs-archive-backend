package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.DocConfig;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Class DocService is service layer interface to work with documents.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
public interface DocService {

  /**
   * A method to find a document using provided id.
   *
   * @param id identificator of document
   * @return the searched document or NotFoundException
   */
  Doc findDocById(String id);

  /**
   * A method to find/get all documents.
   *
   * @return List of Documents
   */
  List<Doc> findAllDocs();

  /**
   * Calls many other methods to create new Record in DB and also sends it to be processed by OCR
   * sooner or later.
   *
   * @param doc to be saved & processed
   * @param docConfig settings for OCR, how to scan the document
   * @return the saved Document with assigned ID and details
   */
  Doc createNewDoc(Doc doc);

  /**
   * Saves document using repo layer.
   *
   * @param doc object to be saved
   * @return the saved document
   */
  Doc saveDoc(Doc doc);

  /**
   * Updates document using repo layer.
   *
   * @param doc object to be updated using provided Id in document
   */
  void updateDocument(Doc doc);

  /**
   * TODO
   * @param files
   * @param docConfig
   * @return
   * @throws IOException
   */
  List<Doc> processNewDocs(MultipartFile[] files, DocConfig docConfig) throws IOException;

  void deleteDocumentById(String id);

  List<Doc> finDocumentsByAsyncApiState(AsyncApiState asyncApiState);


  Page<Doc> findDocsUsingSearchParams(SearchDocParams searchDocParams, Integer page, Integer size);
}
