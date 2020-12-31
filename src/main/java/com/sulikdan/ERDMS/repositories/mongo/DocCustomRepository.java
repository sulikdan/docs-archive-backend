package com.sulikdan.ERDMS.repositories.mongo;

import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.entities.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by Daniel Šulik on 11-Sep-20
 *
 * <p>Class DocumentCustomRepository is customized repository that contains self implemented
 * methods.
 */
public interface DocCustomRepository {

  /**
   * Find/filter method to search docs that user requested.
   *
   * @param searchDocParams object containing multiple params that will be used in search. It is
   *     required that provided params are not null!
   * @param user the one created request
   * @return Page of documents that are valid for provided arguments.
   */
  Page<Doc> findDocsByMultipleArgs(SearchDocParams searchDocParams, User user);

  /**
   * Finds documents using full-text search.
   * @param searchDocParams using searchDocParams but expects non-null and not-empty fulltext, pageIndex and pageSize
   * @param user the one created request
   * @return Page of documents that are valid for provided arguments.
   */
  Page<Doc> findDocsByFullText(SearchDocParams searchDocParams, User user);

}
