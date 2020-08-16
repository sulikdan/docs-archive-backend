package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;

import java.util.List;

/**
 * Created by Daniel Šulik on 26-Jul-20
 *
 * <p>Class VirtualStorageService is used for .....
 */
public interface VirtualStorageService {

// Blocking Queue methods
  Document getNextDocument();

  Document popNextDocument();

  void addDocument(Document document);

  void addDocuments(List<Document> documentList);

//  ConcurrentHashMap methods
  void addDocument(String documentId);

  boolean isDocumentUsed(String documentId);

  void deleteDocument(String documentId);
}
