package com.sulikdan.ERDMS.entities.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Daniel Šulik on 11-Oct-20
 * <p>
 * Class JwtRequest is used for .....
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 6179191621133607270L;

    private String username;
    private String password;

}
