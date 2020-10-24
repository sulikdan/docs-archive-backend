package com.sulikdan.ERDMS.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class UnsupportedLanguage is used for unsupported language required from user.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 11-Jul-20
 */
public class UnsupportedLanguageException extends RuntimeException {
  private static final long serialVersionUID = -3832092656848392904L;

  public UnsupportedLanguageException() {
    super();
  }

  public UnsupportedLanguageException(String message) {
    super(message);
  }

  public UnsupportedLanguageException(String message, Throwable cause) {
    super(message, cause);
  }
}
