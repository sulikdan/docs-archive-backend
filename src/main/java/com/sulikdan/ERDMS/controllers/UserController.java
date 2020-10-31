package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.configurations.configs.JwtTokenUtil;
import com.sulikdan.ERDMS.dto.UserDto;
import com.sulikdan.ERDMS.entities.jwt.JwtRequest;
import com.sulikdan.ERDMS.entities.jwt.JwtResponse;
import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.exceptions.NotValidNewUserException;
import com.sulikdan.ERDMS.services.users.ConfirmationTokenService;
import com.sulikdan.ERDMS.services.users.UserService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class UserController is for Users login/logout/registration
 */
@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

  private final UserService userService;
  private final ConfirmationTokenService confirmationTokenService;
  private final AuthenticationManager authenticationManager;
  private final UserService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final ObjectMapper mapper;

  public UserController(
      UserService userService,
      ObjectMapper mapper,
      ConfirmationTokenService confirmationTokenService,
      AuthenticationManager authenticationManager,
      UserService userDetailsService,
      JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.confirmationTokenService = confirmationTokenService;
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.mapper = new ObjectMapper();
  }

  @GetMapping(value ="/hello")
  public String firstPage() {
    return "Hello World";
  }

  @PostMapping(value = "/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
      throws Exception {
    log.info("Inside authenticate");
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    log.info("Ending....");
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

  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> saveUser(@RequestBody UserDto user) throws Exception {

    if (StringUtil.isNullOrEmpty(user.getEmail()))
      throw new NotValidNewUserException("Empty email!");
    if (StringUtil.isNullOrEmpty(user.getUsername()) || user.getUsername().length() <= 5)
      throw new NotValidNewUserException("Empty username or length is less than 5!");
    if (StringUtil.isNullOrEmpty(user.getPassword()) || user.getPassword().length() <= 5)
      throw new NotValidNewUserException("Empty password or length is less than 5!");

    if (userService.loadUserByUserName(user.getUsername()).isPresent()
        || userService.loadUserByEmail(user.getEmail()).isPresent()) {
      throw new NotValidNewUserException("Username or Email is already used!");
    }

    return ResponseEntity.ok(userDetailsService.registerUser(user));
  }

  @ResponseStatus(code = HttpStatus.OK)
  @PostMapping("/register/confirm")
  void confirmMail(@RequestParam("token") String token) {

    Optional<ConfirmationToken> optionalConfirmationToken =
        confirmationTokenService.findConfirmationTokenByToken(token);
    optionalConfirmationToken.ifPresent(userService::confirmUserRegistration);
  }

  @PostMapping(
      value = "/resetAccount",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resetUser(@RequestBody String emailJson) throws Exception {

    String email = mapper.readTree(emailJson).get("email").textValue();

    if (!EmailValidator.getInstance().isValid(email)) {
      return ResponseEntity.badRequest()
          .body(mapper.writeValueAsString("Incorrect email address!"));
    }

    try {

      userService.resetAccount(email);
      return ResponseEntity.ok(
          mapper.writeValueAsString("An confirmation e-mail was sent to your e-mail."));
      //      TODO create special Exception for not know user ...

    } catch (Error e) {
      log.info("WTF???");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping(value = "/resetAccount/{resetToken}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> resetPassword(
      @PathVariable String resetToken, @RequestBody String passwordJson) throws Exception {

    String password = mapper.readTree(passwordJson).get("password").textValue();

    if (password == null || password.isEmpty() || password.length() <= 5) {
      return ResponseEntity.badRequest()
          .body(mapper.writeValueAsString("Password length should be above 5 characters"));
    }

    try {
      userService.resetAccountPassword(resetToken, password);
      return ResponseEntity.ok("The account password was reset!");
    } catch (Error e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
