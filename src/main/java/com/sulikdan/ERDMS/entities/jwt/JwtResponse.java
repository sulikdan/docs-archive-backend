package com.sulikdan.ERDMS.entities.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by Daniel Šulik on 11-Oct-20
 * <p>
 * Class JwtResponse is used for .....
 */
@Getter
@Setter
@AllArgsConstructor
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -3672741437407812864L;

    private final String jwtToken;

}
