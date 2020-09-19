package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Doc;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Daniel Šulik on 26-Jul-20
 *
 * <p>Class VirtualStorageServiceImpl is used for .....
 */
@Service
public class VirtualStorageServiceImpl implements VirtualStorageService {

  private final BlockingQueue<Doc> docBlockingQueue = new LinkedBlockingDeque<>();
  private final ConcurrentHashMap<String, Boolean> documentsMapCurrentlyInUse =
      new ConcurrentHashMap<>();

  @Override
  public Doc getNextDoc() {
    return docBlockingQueue.peek();
  }

  @Override
  public Doc popNextDoc() {
    return docBlockingQueue.poll();
  }

  @Override
  public void addDoc(Doc doc) {
    docBlockingQueue.offer(doc);
  }

  @Override
  public void addDocs(List<Doc> docList) {
    docBlockingQueue.addAll(docList);
  }

  @Override
  public void addDoc(String documentId) {
    documentsMapCurrentlyInUse.put(documentId, true);
  }

  @Override
  public boolean isDocUsed(String documentId) {
    return documentsMapCurrentlyInUse.containsKey(documentId);
  }

  @Override
  public void deleteDoc(String documentId) {
    documentsMapCurrentlyInUse.remove(documentId);
  }
}
