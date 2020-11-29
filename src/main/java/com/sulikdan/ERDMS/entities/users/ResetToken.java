package com.sulikdan.ERDMS.entities.users;

import com.sulikdan.ERDMS.entities.users.User;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Daniel Šulik on 18-Oct-20
 *
 * <p>Class ResetToken is used for .....
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resetTokens")
public class ResetToken {

  //  Id used also as token, cause it's unique(or it should be)
  @Id @Builder.Default private String id = new ObjectId().toString();
  private LocalDate createdDate;

  @DBRef
  private User user;

  public ResetToken(User user) {
    this.user = user;
    this.createdDate = LocalDate.now();
  }
}
