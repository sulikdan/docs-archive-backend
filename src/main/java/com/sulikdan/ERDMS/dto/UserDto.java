package com.sulikdan.ERDMS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

/**
 * Created by Daniel Šulik on 11-Oct-20
 *
 * <p>Class UserDto is used for .....
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String username;
  @Email private String email;
  private String password;

  public UserDto(UserDto userDto) {
    this.username = userDto.username;
    this.email = userDto.email;
    this.password = userDto.password;
  }
}
