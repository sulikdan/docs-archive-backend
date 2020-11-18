package com.sulikdan.ERDMS.entities;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.TextIndexed;

/**
 * Class Page is used to save pages as string. Later may be extended with more properties per page.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Getter
@Setter
@QueryEntity
@NoArgsConstructor
@AllArgsConstructor
public class DocPage {

//  @TextIndexed
  private String content;
}
