package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.entities.ConfirmationToken;
import com.sulikdan.ERDMS.entities.User;
import com.sulikdan.ERDMS.services.ConfirmationTokenService;
import com.sulikdan.ERDMS.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class UserController is for Users login/logout/registration
 */
@Slf4j
@RestController
@RequestMapping(value = "api/users")
public class UserController {

  private final UserService userService;
  private final ConfirmationTokenService confirmationTokenService;
  private final ObjectMapper mapper;

  public UserController(
      UserService userService,
      ObjectMapper mapper,
      ConfirmationTokenService confirmationTokenService) {
    this.userService = userService;
    this.confirmationTokenService = confirmationTokenService;
    this.mapper = new ObjectMapper();
  }

  @GetMapping("/login")
  String signIn() {

    return "login";
  }

  @GetMapping("/sign-up")
  String signUpPage(User user) {

    return "sign-up";
  }

  @PostMapping("/sign-up")
  String signUp(String user) throws JsonProcessingException {
    log.info("Sign-up with data: " + user.toString());
    User user1 = mapper.readValue(user, User.class);
    userService.registerUser(user1);

    return "redirect:/login";
  }

  @GetMapping("/registration/confirm")
  String confirmMail(@RequestParam("token") String token) {

    Optional<ConfirmationToken> optionalConfirmationToken =
        confirmationTokenService.findConfirmationTokenByToken(token);
    optionalConfirmationToken.ifPresent(userService::confirmUserRegistration);

    return "redirect:/sign-in";
  }
}
