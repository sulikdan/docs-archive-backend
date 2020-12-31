package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.repositories.mongo.DocCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Class DocRepository is repo using CrudRepository to get access to DB, with all default CRUD
 * operations and more methods.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
public interface DocRepository extends MongoRepository<Doc, String>, DocCustomRepository {
  //  TODO  check if save updates -- looks like its smart AF
  // public interface DocumentRepository extends JpaRepository<Document, String> {

  List<Doc> findDocumentsByAsyncApiInfoAsyncApiState(AsyncApiState asyncApiState);

//  List<Doc> findDocumentsBy(List<String> id);

  Page<Doc> findAllBy(TextCriteria criteria, Pageable pageable);

//  Page<Doc> findAllBy(TextCriteria criteria);
//@Query("{$and:[ {'ownerId': ?0} , {'$text' : { '$search' : ?1}} ]}")
//Page<Doc> searchByByOwnerIdAndText(String ownerId, String keywords,
//                                        Pageable page);

}
