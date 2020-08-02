package com.sulikdan.ERDMS.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Class AsyncDocument is used for .....
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 */
@Getter
@Setter
public class AsyncDocument {

  protected AsyncStatus asyncStatus;
  protected String ocrApiDocStatus;
  protected String ocrApiDocResult;

  public AsyncDocument() {
    this.asyncStatus     = AsyncStatus.WAITING_TO_SEND;
    this.ocrApiDocStatus = "";
    this.ocrApiDocResult = "";
  }
}
