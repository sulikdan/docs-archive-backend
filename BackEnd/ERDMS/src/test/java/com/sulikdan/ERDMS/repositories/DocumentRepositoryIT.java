package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Created by Daniel Šulik on 23-Jul-20
 * <p>
 * Class DocumentRepositoryIT is used for .....
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
public class DocumentRepositoryIT {

    @Autowired
    DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
//        documentRepository.
    }

    @Test
    void findDocumentById(){
        Optional<Document> documentOptional =documentRepository.findById("11xyz11");

        Assert.isTrue(documentOptional.isPresent());
//        Assert.notNull(documentOptional.);
        System.out.println(documentOptional.toString());
    }

    @Test
    void saveDocument(){

    }

    @Test
    void saveAllDocuments(){

    }

    @Test
    void deleteDocumentById(){

    }

}
