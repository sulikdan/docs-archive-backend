package com.sulikdan.ERDMS.bootstrap;

import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Class DocBootstrap is used for .....
 *
 * <p>* @author Daniel Šulik * @version 1.0 * @since 22-Jul-20
 */
@Slf4j
@Component
public class DocBootstrap implements ApplicationListener<ContextRefreshedEvent> {

  private final DocService docService;
  private final FileStorageService fileStorageService;

  private final DocRepository documentRepository;

  public DocBootstrap(
      DocService docService,
      FileStorageService fileStorageService,
      DocRepository documentRepository) {
    this.docService         = docService;
    this.fileStorageService = fileStorageService;
    this.documentRepository = documentRepository;
  }

  @Override
  //    @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    log.info("Loading documents.");

    List<Doc> docs = loadDocuments();

    docs.forEach(document -> docService.saveDoc(document));
//    documentRepository.saveAll(documents);

    log.info("Documents should be loaded and stored");
  }

  public List<Doc> loadDocuments() {

    Doc doc1 =
        Doc.builder()
           .id("11xyz11")
           .nameOfFile("JustRandomFile1.jpg")
           .pageList(null)
           .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
           .build();

    Doc doc2 =
        Doc.builder()
           .id("22xyz22")
           .nameOfFile("JustRandomFile2.jpg")
           .pageList(null)
           .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
           .build();
    Doc doc3 =
        Doc.builder()
           .id("33xyz33")
           .nameOfFile("JustRandomFile3.jpg")
           .pageList(null)
           .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
           .build();

    return Arrays.asList(doc1, doc2, doc3);
  }
}
