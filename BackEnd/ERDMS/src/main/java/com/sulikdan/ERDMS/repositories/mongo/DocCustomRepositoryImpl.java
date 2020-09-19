package com.sulikdan.ERDMS.repositories.mongo;

import com.querydsl.core.BooleanBuilder;
import com.sulikdan.ERDMS.entities.AsyncApiState;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.QDoc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Daniel Šulik on 11-Sep-20
 *
 * <p>Class DocumentCustomRepositoryImpl is used for .....
 */
@Slf4j
@Repository
public class DocCustomRepositoryImpl implements DocCustomRepository {

  private final DocMongoRepository mongoRepository;

  public DocCustomRepositoryImpl(DocMongoRepository mongoRepository) {
    this.mongoRepository = mongoRepository;
  }

  @Override
  public List<Doc> findDocsByMultipleArgs(SearchDocParams searchDocParams, int page, int size) {

    QDoc qDocument = new QDoc("doc");
    BooleanBuilder builder = new BooleanBuilder();

//    log.warn(String.valueOf(mongoRepository.findAll().size()));

    //    Ids
    BooleanBuilder builderIds = new BooleanBuilder();
    for (String id : searchDocParams.getIds()) {
      builderIds.or(qDocument.id.contains(id));
//      builderIds.or(qDocument.id.)
    }
    builder.and(builderIds);

    //    States
    BooleanBuilder builderStates = new BooleanBuilder();
    for (AsyncApiState state : searchDocParams.getStates()) {
      builderStates.or(qDocument.asyncApiInfo.asyncApiState.eq(state));
    }
    builder.and(builderStates);

    //    Languages
    BooleanBuilder builderLanguages = new BooleanBuilder();
    for (String language : searchDocParams.getLanguages()) {
      builderLanguages.or(qDocument.docConfig.lang.eq(language));
    }
    builder.and(builderLanguages);

    //    TODO add isShared property with users!!
    //    if (  isShared != null){
    //      builder.and(qDocument)
    //    }

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

    Pageable pageable = null;
    if (sort != null) {
      if (searchDocParams.getSortAscending()) {
        pageable = PageRequest.of(page, size, sort.ascending());
      } else {
        pageable = PageRequest.of(page, size, sort.descending());
      }
    } else {
      pageable = PageRequest.of(page, size);
    }

    System.out.println("Builder values:\n"+builder.getValue().toString());
    System.out.println(pageable.toString());

//    if (builder.hasValue()) {
//      System.out.println("Was here without values...");
      return (List<Doc>) mongoRepository.findAll(builder, pageable).getContent();
//      }
//    else return (List<Doc>) mongoRepository.findAll(pageable);
  }
}
