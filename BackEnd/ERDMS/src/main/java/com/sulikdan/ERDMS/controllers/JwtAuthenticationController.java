package com.sulikdan.ERDMS.controllers;

import com.sulikdan.ERDMS.configurations.configs.JwtTokenUtil;
import com.sulikdan.ERDMS.dto.UserDto;
import com.sulikdan.ERDMS.entities.jwt.JwtRequest;
import com.sulikdan.ERDMS.entities.jwt.JwtResponse;
import com.sulikdan.ERDMS.services.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Daniel Šulik on 11-Oct-20
 *
 * <p>Class JwtAuthenticationController is used for .....
 */
@CrossOrigin
@AllArgsConstructor
@RestController
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @PostMapping(value = "/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
      throws Exception {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token));
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

  @PostMapping(value = "/register")
  public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {
    return ResponseEntity.ok(userDetailsService.registerUser(user));
  }
}
