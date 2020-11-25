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
import io.swagger.v3.oas.annotations.Operation;
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
//  private final UserService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final ObjectMapper mapper;

  public UserController(
      UserService userService,
      ObjectMapper mapper,
      ConfirmationTokenService confirmationTokenService,
      AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.confirmationTokenService = confirmationTokenService;
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.mapper = new ObjectMapper();
  }

  /**
   * Testing endpoint for the controller
   * @return Hello world
   */
  @Operation(summary = "An endpoint to test availability of controller.")
  @GetMapping(value ="/hello")
  public String firstPage() {
    return "Hello World";
  }

  /**
   * An endpoint to authenticate or create new authentication jwtToken.
   * @param authenticationRequest object containing username and password
   * @return jwtToken
   * @throws Exception
   */
  @Operation(summary = "An endpoint to authenticate or create new authentication jwtToken.")
  @PostMapping(value = "/authenticate")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
      throws Exception {
    log.info("Inside authenticate");
    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails =
        userService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    log.info("Ending....");
    return ResponseEntity.ok(new JwtResponse(token));
  }

  /**
   * A method to authenticate current log-in username and passowrd.
   * @param username
   * @param password
   * @throws Exception
   */
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

  /**
   * An endpoint to register new account.
   * @param userDto new account data
   * @return registered account
   * @throws Exception
   */
  @Operation(summary = "An endpoint to register new account.")
  @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) throws Exception {

    if (StringUtil.isNullOrEmpty(userDto.getEmail()))
      throw new NotValidNewUserException("Empty email!");
    if (StringUtil.isNullOrEmpty(userDto.getUsername()) || userDto.getUsername().length() <= 5)
      throw new NotValidNewUserException("Empty username or length is less than 5!");
    if (StringUtil.isNullOrEmpty(userDto.getPassword()) || userDto.getPassword().length() <= 5)
      throw new NotValidNewUserException("Empty password or length is less than 5!");

    if (userService.loadUserByUserName(userDto.getUsername()).isPresent()
        || userService.loadUserByEmail(userDto.getEmail()).isPresent()) {
      throw new NotValidNewUserException("Username or Email is already used!");
    }

    UserDto userDto2 = userService.registerUser(userDto);
    return ResponseEntity.ok(userDto2);
  }

  /**
   * Confirmation endpoint to register an account.
   * @param token is confirmation string.
   */
  @Operation(summary = "Confirmation endpoint to register an account.")
  @ResponseStatus(code = HttpStatus.OK)
  @PostMapping("/register/confirm")
  void confirmMail(@RequestParam("token") String token) {

    Optional<ConfirmationToken> optionalConfirmationToken =
        confirmationTokenService.findConfirmationTokenByToken(token);
    optionalConfirmationToken.ifPresent(userService::confirmUserRegistration);
  }

  /**
   * Used to begin a process of reseting account.
   * @param emailJson email address of account that is going to be reset.
   * @return Result string status.
   * @throws Exception
   */
  @Operation(summary = "Used to begin a process of reseting account.")
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
      log.info("Account was not reset, because of error.");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  /**
   * Used to reset password of the user's account.
   * @param resetToken token generated before, to reset account
   * @param passwordJson new password to be used to replace old one
   * @return Status of being reset positively or negatively.
   * @throws Exception
   */
  @Operation(summary = "To reset password.")
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
      return ResponseEntity.status(HttpStatus.OK).body(mapper.writeValueAsString("The account password was reset!"));
    } catch (Error e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapper.writeValueAsString(e.getMessage()));
    }
  }
}
