package com.sulikdan.ERDMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Class AsyncDocument is used for storing date about current doc status on OCR.
 * Careful with URIs changes.
 * @implNote When AsyncApiState changed manually, it may cause Nullpointer exception, while trying
 * to communicate with OCR. Recommended is to changed it only to states WAITING_TO_SEND & COMPLETED.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 22-Jul-20
 *
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncApiInfo {

  @JsonProperty("documentProcessStatus")
  private AsyncApiState asyncApiState;

  @JsonProperty("currentStatusLink")
  private String ocrApiDocStatus;

  @JsonProperty("resultLink")
  private String ocrApiDocResult;

  public AsyncApiInfo() {
    this.asyncApiState   = AsyncApiState.WAITING_TO_SEND;
    this.ocrApiDocStatus = "";
    this.ocrApiDocResult = "";
  }

  public AsyncApiInfo(AsyncApiState asyncApiState, String ocrApiDocStatus, String ocrApiDocResult) {
    this.asyncApiState   = asyncApiState;
    this.ocrApiDocStatus = ocrApiDocStatus;
    this.ocrApiDocResult = ocrApiDocResult;
  }

  @Override
  public String toString() {
    return "AsyncDocInfo{"
        + "asyncState="
        + asyncApiState
        + ", ocrApiDocStatus='"
        + ocrApiDocStatus
        + '\''
        + ", ocrApiDocResult='"
        + ocrApiDocResult
        + '\''
        + '}';
  }
}
