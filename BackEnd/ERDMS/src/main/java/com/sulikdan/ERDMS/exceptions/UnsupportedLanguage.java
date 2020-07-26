package com.sulikdan.ERDMS.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * <p>Class UnsupportedLanguage is used for .....
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 11-Jul-20
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedLanguage extends RuntimeException {
  public UnsupportedLanguage() {
    super();
  }

  public UnsupportedLanguage(String message) {
    super(message);
  }

  public UnsupportedLanguage(String message, Throwable cause) {
    super(message, cause);
  }
}
