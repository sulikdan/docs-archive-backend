package com.sulikdan.ERDMS.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Daniel Šulik on 03-Sep-20
 * <p>
 * Class User is used for .....
 */
@Getter
@Setter
@Builder
@Document(collection = "users")
public class User {

    @Id
    @Builder.Default
    private String id = new ObjectId().toString();;

    private String firstName;
    private String lastName;

    private String email;
//    Hashed!
    private String password;

    public User(String id, String firstName, String lastName, String email, String password) {
        this.id        = id;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.password  = password;
    }
}
