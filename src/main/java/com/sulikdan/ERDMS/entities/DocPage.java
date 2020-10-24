package com.sulikdan.ERDMS.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class Page is used to save pages as string. Later may be extended with more properties per page.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 18-Jul-20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocPage {

  private String content;
}
