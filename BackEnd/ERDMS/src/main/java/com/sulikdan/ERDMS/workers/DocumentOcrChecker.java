package com.sulikdan.ERDMS.workers;

import com.sulikdan.ERDMS.entities.AsyncStatus;
import com.sulikdan.ERDMS.entities.Document;
import com.sulikdan.ERDMS.services.DocumentService;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by Daniel Šulik on 25-Jul-20
 * <p>
 * Class DocumentOcrChecker is used for .....
 */
@Async("threadPoolTaskExecutor")
public class DocumentOcrChecker implements Runnable {

    protected Document document;
    protected AsyncStatus asyncStatus;

    protected DocumentService documentService;


    @Override
    public void run() {

    }
}
