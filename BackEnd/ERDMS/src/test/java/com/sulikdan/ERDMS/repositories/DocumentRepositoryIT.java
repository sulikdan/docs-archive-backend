package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.Assert;

import javax.print.Doc;
import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Class DocumentRepositoryIT is IT(Integration Test) for DocumentRepository.
 * Mainly to check if the connection works.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 23-Jul-20
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class DocumentRepositoryIT {

    @Autowired
    DocumentRepository documentRepository;

    List<Document> documentList ;

    public DocumentRepositoryIT() {
        Document document1 = Document.builder().id("11xyz11").nameOfFile("JustRandomFile1.jpg").documentFile(null).pageList(null).build();
        Document document2 =
                Document.builder().id("22xyz22").nameOfFile("JustRandomFile2.jpg").documentFile(null).pageList(null).build();
//                Document.builder().id(new ObjectId().toString()).nameOfFile("JustRandomFile2.jpg").documentFile(null).pageList(null).build();
        Document document3 = Document.builder().id("33xyz33").nameOfFile("JustRandomFile3.jpg").documentFile(null).pageList(null).build();

        documentList = Arrays.asList(document1,document2,document3);
    }

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();

        documentRepository.save(documentList.get(0));
    }

    @Test
    void findDocumentById(){
        Optional<Document> documentOptional = documentRepository.findById("11xyz11");

        Assert.assertTrue(documentOptional.isPresent());
        Document documentRetrieved = documentOptional.get();

        Assert.assertEquals(documentList.get(0),documentRetrieved);
    }

    @Test
    void saveDocument(){
        documentRepository.save(documentList.get(1));

        Optional<Document> documentOptional =documentRepository.findById(documentList.get(1).getId());

        Assert.assertTrue(documentOptional.isPresent());
        Document documentRetrieved = documentOptional.get();

        Assert.assertEquals(documentList.get(1),documentRetrieved);
    }

    @Test
    void saveAllDocuments(){

        // cleaning whole Doc repo
        documentRepository.deleteById(documentList.get(0).getId());

        Iterable<Document> documentIterable = documentRepository.findAll();
        Assert.assertFalse(documentIterable.iterator().hasNext());

        documentRepository.saveAll(documentList);

        documentIterable = documentRepository.findAll();
        Assert.assertTrue(documentIterable.iterator().hasNext());
    }

    @Test
    void deleteDocumentById(){

        documentRepository.deleteById(documentList.get(0).getId());

        Optional<Document> documentOptional =documentRepository.findById("11xyz11");
        Assert.assertFalse(documentOptional.isPresent());
    }

}
