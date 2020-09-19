package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.repositories.mongo.DocCustomRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class DocumentRepository is repo using CrudRepository to get access to DB, with all default CRUD
 * operations and more methods.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
// @Repository
//public interface DocRepository extends CrudRepository<Doc, String> {
public interface DocRepository extends MongoRepository<Doc, String>, DocCustomRepository {
//  TODO  check if save updates -- looks like its smart AF
  // public interface DocumentRepository extends JpaRepository<Document, String> {

    List<Doc> findDocumentsByAsyncApiInfoAsyncApiState(AsyncApiState asyncApiState);

    List<Doc> findDocumentsBy  (List<String> id);


}
