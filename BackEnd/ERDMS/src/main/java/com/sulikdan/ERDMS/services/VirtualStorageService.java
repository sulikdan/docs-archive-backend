package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Doc;

import java.util.List;

/**
 * Created by Daniel Šulik on 26-Jul-20
 *
 * <p>Class VirtualStorageService is used for .....
 */
public interface VirtualStorageService {

// Blocking Queue methods
  Doc getNextDoc();

  Doc popNextDoc();

  void addDoc(Doc doc);

  void addDocs(List<Doc> docList);

//  ConcurrentHashMap methods
  void addDoc(String documentId);

  boolean isDocUsed(String documentId);

  void deleteDoc(String documentId);

// Error ConcurrentHashMap
  void addOrIncreaseFailedDoc(String documentId);

  int docFailedTimes(String documentId);

  void deleteFailedDoc(String documentId);
}
