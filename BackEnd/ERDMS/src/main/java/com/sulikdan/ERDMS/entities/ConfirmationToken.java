package com.sulikdan.ERDMS.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class ConfirmationToken is used for .....
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "confirmationTokens")
public class ConfirmationToken {

    @Id
    @Builder.Default
    private String id = new ObjectId().toString();

    private String confirmationToken;

    private LocalDate createdDate;

//    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "user_id")
    @DBRef
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        this.createdDate = LocalDate.now();
        this.confirmationToken = UUID.randomUUID().toString();
    }
}