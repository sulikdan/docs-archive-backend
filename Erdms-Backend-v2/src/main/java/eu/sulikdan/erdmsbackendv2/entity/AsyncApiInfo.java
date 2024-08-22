package eu.sulikdan.erdmsbackendv2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Class AsyncApiInfo is used for storing date about current doc status on OCR.
 * Careful with URIs changes.
 * @implNote When AsyncApiState changed manually, it may cause Nullpointer exception, while trying
 * to communicate with OCR. Recommended is to changed it only to states WAITING_TO_SEND & COMPLETED.
 *
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AsyncApiInfo {

  @Id
  private UUID id = UUID.randomUUID();

  @Enumerated(EnumType.STRING)
  private AsyncApiState asyncApiState;

  private String ocrApiDocStatus;

  private String ocrApiDocResult;

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
