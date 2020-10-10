package com.sulikdan.ERDMS.repositories.mongo;

import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Daniel Šulik on 11-Sep-20
 *
 * <p>Class DocumentCustomRepository is used for .....
 */
public interface DocCustomRepository {

  Page<Doc> findDocsByMultipleArgs(SearchDocParams searchDocParams);
}
