package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.TessApiDoc;

/**
 * Created by Daniel Šulik on 11-Aug-20
 *
 * <p>Class RestApiOcr is used for Communication with OCR
 */
public interface RestApiOcr {

  /**
   * Sends document for extraction
   * @param doc
   * @return
   * @throws JsonProcessingException
   */
  AsyncApiInfo postDocRequest(Doc doc) throws JsonProcessingException;

  /**
   * Gets actual status of the Document extraction.
   * @param statusUri
   * @return
   * @throws JsonProcessingException
   */
  AsyncApiInfo getDocStatus(String statusUri) throws JsonProcessingException;

  /**
   * Donwnload extracted part from the document.
   * @param resultUri
   * @return
   * @throws JsonProcessingException
   */
  TessApiDoc getDocResult(String resultUri) throws JsonProcessingException;

  /**
   * Deletes resources of already extracted documents.
   * @param deleteUri
   * @return
   */
  boolean deleteDoc(String deleteUri);
}
