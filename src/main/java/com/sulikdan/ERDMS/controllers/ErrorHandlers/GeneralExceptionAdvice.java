package com.sulikdan.ERDMS.controllers.ErrorHandlers;

// import net.sourceforge.tess4j.TesseractException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sulikdan.ERDMS.exceptions.DocNotFoundException;
import com.sulikdan.ERDMS.exceptions.InvalidAccessRightException;
import com.sulikdan.ERDMS.exceptions.NotValidNewUserException;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * Class DocUploadExceptionAdvice is used to take care of exceptions produced on backend and transforms them to simpler
 * desciption readable for user.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 09-Jul-20
 */
@ControllerAdvice
public class GeneralExceptionAdvice extends ResponseEntityExceptionHandler {

  //    @ExceptionHandler(TesseractException.class)
  //    public ResponseEntity<String> handleTesseractException(Exception e) {
  //        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new
  // String("Tesseract exception:\n" + e.getMessage()));
  //    }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<String> handleTesseractException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Troubles with handling Tesseract API.");
  }

  @ExceptionHandler(UnsupportedLanguageException.class)
  public ResponseEntity<String> handleUnsupportedLanguageException(Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .body("Unsupported language!");
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Error, while reading uploaded file!");
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new String("File too large!"));
  }

  @ExceptionHandler(NotValidNewUserException.class)
  public ResponseEntity<String> handleNotValidUserException(Exception e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(InvalidAccessRightException.class)
  public ResponseEntity<String> handleInvalidAccessRightException(Exception e){
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
  }

  @ExceptionHandler(DocNotFoundException.class)
  public ResponseEntity<String> handleDocNotFoundException(Exception e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }
}
