package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Daniel Šulik on 26-Jul-20
 * <p>
 * Class VirtualStorageServiceImpl is used for .....
 */
@Service
public class VirtualStorageServiceImpl implements VirtualStorageService {

    BlockingQueue<Document> documentBlockingQueue = new LinkedBlockingDeque<>();

    @Override
    public Document getNextDocument() {
        return documentBlockingQueue.peek();
    }

    @Override
    public Document popNextDocument() {
        return documentBlockingQueue.poll();
    }

    @Override
    public void addDocument(Document document) {
        documentBlockingQueue.offer(document);
    }

    @Override
    public void addDocuments(List<Document> documentList) {
        documentBlockingQueue.addAll(documentList);
    }
}
