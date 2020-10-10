package com.sulikdan.ERDMS.controllers.ErrorHandlers;

// import net.sourceforge.tess4j.TesseractException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sulikdan.ERDMS.exceptions.UnsupportedLanguage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * Class DocUploadExceptionAdvice is used for .....
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 09-Jul-20
 */
@ControllerAdvice
public class DocUploadExceptionAdvice extends ResponseEntityExceptionHandler {

  //    @ExceptionHandler(TesseractException.class)
  //    public ResponseEntity<String> handleTesseractException(Exception e) {
  //        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new
  // String("Tesseract exception:\n" + e.getMessage()));
  //    }

  @ExceptionHandler(JsonProcessingException.class)
  public ResponseEntity<String> handleTesseractException(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(JsonProcessingException.class.getName() + " exception:\n" + e.getMessage());
  }

  @ExceptionHandler(UnsupportedLanguage.class)
  public ResponseEntity<String> handleUnsupportedLanguage(Exception e) {
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
        .body(UnsupportedLanguage.class.getName() + " exception:\n" + e.getMessage());
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<String> handleIOException(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(IOException.class.getName() + " exception:\n" + e.getMessage());
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new String("File too large!"));
  }
}
