package com.sulikdan.ERDMS.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Daniel Šulik on 03-Sep-20
 * <p>
 * Class User is used for .....
 */
@Getter
@Setter
public class User {
    private String id;

    private String firstName;
    private String lastName;

    private String email;
//    Hashed!
    private String password;
}
