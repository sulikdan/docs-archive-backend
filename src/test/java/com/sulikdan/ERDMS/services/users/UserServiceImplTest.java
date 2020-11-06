package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.configurations.properties.FrontEndProperties;
import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.entities.users.ResetToken;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.repositories.UserRepository;
import com.sulikdan.ERDMS.services.EmailService;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

  @Mock UserRepository userRepository;
  @Mock ConfirmationTokenService confirmationTokenService;
  @Mock ResetTokenService resetTokenService;
  @Mock EmailService emailService;
  @Mock PasswordEncoder bcryptEncoder;

  @Mock FrontEndProperties frontEndProperties;

  UserService userService;

  User user;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    userService =
        new UserServiceImpl(
            userRepository,
            confirmationTokenService,
            resetTokenService,
            emailService,
            bcryptEncoder,
            frontEndProperties);

    user = new User();
    user.setEmail("yoloz@gmail.com");
    user.setUsername("matrix");
    user.setPassword("matrix");
  }

  @Test
  void registerUser() {
    // given
    ConfirmationToken token = new ConfirmationToken();

    when(bcryptEncoder.encode(anyString())).thenReturn(user.getPassword());
    when(userRepository.save(any(User.class))).thenReturn(user);
    doNothing().when(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));

    // when
    userService.registerUser(user);

    // then
    verify(bcryptEncoder).encode(anyString());
    verify(userRepository).save(any());
    verify(confirmationTokenService).saveConfirmationToken(any());
  }

  @Test
  void confirmUserRegistration() {
    // given
    ConfirmationToken token = new ConfirmationToken();
    token.setUser(user);

    when(userRepository.save(any(User.class))).thenReturn(user);
    doNothing().when(confirmationTokenService).deleteConfirmationToken(anyString());

    // when
    userService.confirmUserRegistration(token);

    // then
    verify(userRepository).save(any());
    verify(confirmationTokenService).deleteConfirmationToken(any());
  }

  //  @Test
  //  void resetAccountNotFound() {
  //    // given
  //
  //    when(userRepository.findByEmail(anyString()))
  //        .thenThrow(new RuntimeException("User not found!"));
  //
  //    // when
  //    //    tr
  //    userService.resetAccount("yolo@gmail.com");
  //
  //    // then
  //    //    then(RuntimeException())
  //
  //  }

  @Test
  void resetAccount() {
    // given

    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

    doNothing().when(resetTokenService).saveResetToken(any());
    doNothing().when(emailService).sendEmail(any());

    // when
    userService.resetAccount("yolo@gmail.com");

    // then
    verify(userRepository).findByEmail(anyString());
    verify(resetTokenService).saveResetToken(any());
    verify(emailService).sendEmail(any());
  }

  @Test
  void resetAccountPassword() {
    // given
    ResetToken resetToken = new ResetToken();
    resetToken.setUser(user);
    //    resetToken.setCreatedDate(LocalDate.now());

    when(resetTokenService.findTokenById(anyString())).thenReturn(Optional.of(resetToken));
    doNothing().when(resetTokenService).deleteResetToken(anyString());
    when(userRepository.save(any())).thenReturn(user);
    doNothing().when(resetTokenService).deleteResetToken(anyString());

    // when
    userService.resetAccountPassword("randomId", "matrix");

    // then
    verify(resetTokenService).findTokenById(anyString());
    verify(userRepository).save(any());
    verify(resetTokenService).deleteResetToken(anyString());
  }

  @Test
  void loadUserByUserName() {
    // given
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    // when
    Optional<User> user = userService.loadUserByUserName("matrix");

    // then
    Assert.assertTrue(user.isPresent());
    verify(userRepository).findByUsername(anyString());
  }

  @Test
  void loadUserByEmail() {
    // given
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

    // when
    Optional<User> user = userService.loadUserByEmail("yolo@gmail.com");

    // then
    Assert.assertTrue(user.isPresent());
    verify(userRepository).findByEmail(anyString());
  }
}
