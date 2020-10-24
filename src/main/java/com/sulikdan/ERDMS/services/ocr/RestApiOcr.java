package com.sulikdan.ERDMS.services.ocr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.TessApiDoc;

/**
 * Created by Daniel Šulik on 11-Aug-20
 *
 * <p>Class RestApiOcr is used for .....
 */
public interface RestApiOcr {

  AsyncApiInfo postDocRequest(Doc doc) throws JsonProcessingException;

  AsyncApiInfo getDocStatus(String statusUri) throws JsonProcessingException;

  TessApiDoc getDocResult(String resultUri) throws JsonProcessingException;

  boolean deleteDoc(String deleteUri);
}
