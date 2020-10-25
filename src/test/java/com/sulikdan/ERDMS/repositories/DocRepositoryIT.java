package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.repositories.mongo.DocCustomRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

/**
 * Class DocumentRepositoryIT is IT(Integration Test) for DocumentRepository. Mainly to check if the
 * connection works.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 23-Jul-20
 */
// @ContextConfiguration(classes={MongoConfig.class})
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataMongoTest
@EnableMongoRepositories(considerNestedRepositories = true)
public class DocRepositoryIT {

  @Autowired private DocRepository documentRepository;
  @Autowired private UserRepository userRepository;

  private DocCustomRepository docCustomRepository;

  private User user;

  List<Doc> docList;

  List<Doc> createDocs(User user) {
    Doc doc1 =
        Doc.builder()
            .id("11xyz11")
            .nameOfFile("JustRandomFile1.jpg")
            .owner(user)
            .docPageList(null)
            .build();
    Doc doc2 =
        Doc.builder()
            .id("22xyz22")
            .nameOfFile("JustRandomFile2.jpg")
            .owner(user)
            .docPageList(null)
            .build();
    Doc doc3 =
        Doc.builder()
            .id("33xyz33")
            .nameOfFile("JustRandomFile3.jpg")
            .owner(user)
            .docPageList(null)
            .build();

    return Arrays.asList(doc1, doc2, doc3);
  }

  @BeforeEach
  void setUp() {

    user =
        User.builder()
            .id("1234")
            .email("yolouser@IdontKnow.com")
            .username("tester")
            .password("tester")
            .build();

    userRepository.save(user);

    docList = createDocs(user);

    documentRepository.deleteAll();

    documentRepository.save(docList.get(0));
    //    docCustomRepository = new DocCustomRepositoryImpl((DocMongoRepository)
    // documentRepository);
  }

  @Test
  void findDocumentById() {
    Optional<Doc> documentOptional = documentRepository.findById("11xyz11");

    Assert.assertTrue(documentOptional.isPresent());
    Doc docRetrieved = documentOptional.get();

    Assert.assertEquals(docList.get(0).getId(), docRetrieved.getId());
  }

  @Test
  void findDocsByMultipleArgsUsingIds() {
    List<String> ids = new ArrayList<>();
    ids.add("11xyz11");

    SearchDocParams docParams = new SearchDocParams();
    docParams.setIds(ids);
    docParams.setPageIndex(0);
    docParams.setPageSize(5);

    documentRepository.save(docList.get(1));
    documentRepository.save(docList.get(2));

    //    mongoRepository.findA
    Page<Doc> pagedDocs = documentRepository.findDocsByMultipleArgs(docParams, user);
    List<Doc> docsList = pagedDocs.getContent();

    Assert.assertNotNull(docsList);
    Assert.assertEquals(ids.size(), docsList.size());
  }

  @Test
  void findDocsByMultipleArgsUsingDescSorting() {
    SearchDocParams docParams = new SearchDocParams();
    docParams.setPageIndex(0);
    docParams.setPageSize(5);
    docParams.setColumnSortList(Collections.singletonList("id"));
    docParams.setSortAscending(false);

    documentRepository.save(docList.get(1));

    Page<Doc> pagedDocsList = documentRepository.findDocsByMultipleArgs(docParams, user);
    List<Doc> docsList = pagedDocsList.getContent();

    Assert.assertNotNull(docsList);
    Assert.assertEquals(2, docsList.size());
    Assert.assertEquals(docList.get(1).getId(), docsList.get(0).getId());
    Assert.assertEquals(docList.get(0).getId(), docsList.get(1).getId());
  }

  @Test
  void saveDocument() {
    documentRepository.save(docList.get(1));

    Optional<Doc> documentOptional = documentRepository.findById(docList.get(1).getId());

    Assert.assertTrue(documentOptional.isPresent());
    Doc docRetrieved = documentOptional.get();

    Assert.assertEquals(docList.get(1).getId(), docRetrieved.getId());
  }

  @Test
  void saveAllDocuments() {

    // cleaning whole Doc repo
    documentRepository.deleteById(docList.get(0).getId());

    Iterable<Doc> documentIterable = documentRepository.findAll();
    Assert.assertFalse(documentIterable.iterator().hasNext());

    documentRepository.saveAll(docList);

    documentIterable = documentRepository.findAll();
    Assert.assertTrue(documentIterable.iterator().hasNext());
  }

  @Test
  void deleteDocumentById() {

    documentRepository.deleteById(docList.get(0).getId());

    Optional<Doc> documentOptional = documentRepository.findById("11xyz11");
    Assert.assertFalse(documentOptional.isPresent());
  }
}
