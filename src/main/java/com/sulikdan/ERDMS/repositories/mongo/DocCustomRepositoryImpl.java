package com.sulikdan.ERDMS.repositories.mongo;

import com.querydsl.core.BooleanBuilder;
import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.entities.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Daniel Šulik on 11-Sep-20
 *
 * <p>Class DocumentCustomRepositoryImpl is implementation of an interface.
 *
 * @see DocCustomRepository
 */
@Slf4j
@Repository
public class DocCustomRepositoryImpl implements DocCustomRepository {

  private final DocMongoRepository mongoRepository;

  private final MongoTemplate mongoTemplate;

  public DocCustomRepositoryImpl(
          DocMongoRepository mongoRepository, MongoTemplate mongoTemplate) {
    this.mongoRepository = mongoRepository;
    this.mongoTemplate   = mongoTemplate;

    TextIndexDefinition textIndex =
            new TextIndexDefinition.TextIndexDefinitionBuilder().onAllFields().build();
    mongoTemplate.indexOps(Doc.class).ensureIndex(textIndex);
  }

  @Override
  public Page<Doc> findDocsByMultipleArgs(SearchDocParams searchDocParams, User user) {

//    String objectId = ObjectId().toString().substring(10,35);

    QDoc qDocument = new QDoc("doc");
    BooleanBuilder builder = new BooleanBuilder();

    //    Ids
//    BooleanBuilder builderIds = new BooleanBuilder();
//    for (String id : searchDocParams.getIds()) {
//      builderIds.or(qDocument.id.);
//    }
//    builder.and(builderIds);


    // Text
    BooleanBuilder builderText = new BooleanBuilder();
    QDocPage qDocPage = new QDocPage("docPage");
    for (String text : searchDocParams.getSearchedText()) {
      builderText.or(qDocument.docPageList.any().content.containsIgnoreCase(text));
    }

    builder.and(builderText);

    //    States
    BooleanBuilder builderStates = new BooleanBuilder();
    for (AsyncApiState state : searchDocParams.getStates()) {
      builderStates.or(qDocument.asyncApiInfo.asyncApiState.eq(state));
    }
    builder.and(builderStates);

    //    Languages
    BooleanBuilder builderLanguages = new BooleanBuilder();
    for (String language : searchDocParams.getLanguages()) {
      builderLanguages.or(qDocument.docConfig.lang.equalsIgnoreCase(language));
    }
    builder.and(builderLanguages);

    //    Tags
    BooleanBuilder builderTags = new BooleanBuilder();
    for (Tag tag : searchDocParams.getTags()) {
      builderLanguages.or(qDocument.tags.contains(tag));
    }
    builder.and(builderTags);

    //    Is shared - i.e. if user want to see all shared docs
    if (searchDocParams.getIsShared() != null) {
      builder.and(qDocument.isShared.eq(searchDocParams.getIsShared()));
    }

    //    Ownership or sharing-ship - all doc he owns or all docs that are shared to him
    BooleanBuilder builderOwner = new BooleanBuilder();

    builderOwner.or(qDocument.isShared.eq(true));
    builderOwner.or(qDocument.owner.id.eq(user.getId()));
    builder.and(builderOwner);

    if (searchDocParams.getCreatedFrom() != null) {
      builder.and(qDocument.createDateTime.goe(searchDocParams.getCreatedFrom()));
    }

    if (searchDocParams.getCreatedTo() != null) {
      builder.and(qDocument.createDateTime.loe(searchDocParams.getCreatedTo()));
    }

    if (searchDocParams.getUpdatedFrom() != null) {
      builder.and(qDocument.createDateTime.goe(searchDocParams.getUpdatedFrom()));
    }

    if (searchDocParams.getUpdatedTo() != null) {
      builder.and(qDocument.createDateTime.loe(searchDocParams.getUpdatedTo()));
    }

    Sort sort = null;
    for (String column : searchDocParams.getColumnSortList()) {
      if (sort == null) sort = Sort.by(column);
      else sort = sort.and(Sort.by(column));
    }

    // TODO sort asc || desc not working when empty
    int page = searchDocParams.getPageIndex();
    int size = searchDocParams.getPageSize();

    Pageable pageable = null;
    if (sort != null) {
      if (searchDocParams.getSortAscending()) {
        pageable = PageRequest.of(page, size, sort.ascending());
      } else {
        pageable = PageRequest.of(page, size, sort.descending());
      }
    } else {
      pageable = PageRequest.of(page, size);
      //      mongoRepository.
    }

    System.out.println("Builder values:\n" + builder.getValue().toString());
    System.out.println(pageable.toString());

    //    if (builder.hasValue()) {
    //      System.out.println("Was here without values...");
    return mongoRepository.findAll(builder, pageable);
  }

  @Override
  public Page<Doc> findDocsByFullText(SearchDocParams searchDocParams, User user) {
    PageRequest pageRequest =
            PageRequest.of(searchDocParams.getPageIndex(), searchDocParams.getPageSize());
    Query query =
            TextQuery.query(
                    TextCriteria.forDefaultLanguage().matchingAny(searchDocParams.getFullText()))
                     .with(pageRequest);
    Criteria orCriteria = new Criteria();

    query.addCriteria(
            orCriteria.orOperator(
                    Criteria.where("owner").is(user), (Criteria.where("isShared").is(Boolean.TRUE))));
    List<Doc> list = mongoTemplate.find(query, Doc.class);
    long count = mongoTemplate.count(query, Doc.class);
    return new PageImpl<Doc>(list, pageRequest, count);
  }
}
