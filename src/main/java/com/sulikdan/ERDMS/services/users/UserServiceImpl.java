package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.configurations.properties.FrontEndProperties;
import com.sulikdan.ERDMS.dto.UserDto;
import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.entities.users.ResetToken;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.repositories.UserRepository;
import com.sulikdan.ERDMS.services.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/** Created by Daniel Šulik on 10-Oct-20 */
@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ConfirmationTokenService confirmationTokenService;
  private final ResetTokenService resetTokenService;
  private final EmailService emailService;
  private final PasswordEncoder bcryptEncoder;

  private final FrontEndProperties frontEndProperties;

  //  private static final String backEndDomainUrl = "http://localhost:8081";
  //  private static final String frontEndDomainUrl = "http://localhost:4200";

  @Override
  public void registerUser(User user) {

    String encryptedPassword = bcryptEncoder.encode(user.getPassword());

    user.setPassword(encryptedPassword);

    User createdUser = userRepository.save(user);

    ConfirmationToken confirmationToken = new ConfirmationToken(user);

    confirmationTokenService.saveConfirmationToken(confirmationToken);

    sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
  }

  @Override
  public UserDto registerUser(UserDto userDto) {
    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setUsername(userDto.getUsername());
    user.setPassword(userDto.getPassword());

    registerUser(user);

    return userDto;
  }

  @Override
  public void confirmUserRegistration(ConfirmationToken confirmationToken) {

    User user = confirmationToken.getUser();

    user.setEnabled(true);

    userRepository.save(user);

    confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //    Optional<User> optionalUser = userRepository.findByEmail(email);
    Optional<User> optionalUser = userRepository.findByUsername(username);

    if (optionalUser.isPresent()) {
      User foundUser = optionalUser.get();
      return new org.springframework.security.core.userdetails.User(
          foundUser.getUsername(),
          foundUser.getPassword(),
          foundUser.getEnabled(),
          true,
          true,
          !foundUser.getLocked(),
          new ArrayList<>());
    } else {
      throw new UsernameNotFoundException(
          MessageFormat.format("User with email {0} cannot be found.", username));
    }
  }

  void sendConfirmationMail(String userMail, String token) {
    final String frontEndDomainUrl =
        "http://" + frontEndProperties.getAddress() + ":" + frontEndProperties.getPort();

    String subject = "Confirmation link to DocsArchive!";
    String mailText =
        "Welcome to the DocsArchive!\n"
            + "Thank you for registering.\n "
            + "Please click on the below link to activate your DocsArchive account.\n"
            + frontEndDomainUrl
            + "#/auth/login?token=";

    //                    + backEndDomainUrl  +"/api/users/register/confirm?token=";

    final SimpleMailMessage emailMessage = createEmailMessage(userMail, subject, mailText, token);

    emailService.sendEmail(emailMessage);
  }

  @Override
  public void resetAccount(String email) {
    Optional<User> user = userRepository.findByEmail(email);

    if (!user.isPresent()) {
      throw new RuntimeException("User not found!");
    }

    final String frontEndDomainUrl =
        "http://" + frontEndProperties.getAddress() + ":" + frontEndProperties.getPort();

    //    create new token
    ResetToken resetToken = new ResetToken(user.get());

    //    save it
    resetTokenService.saveResetToken(resetToken);

    //    send token through email
    String subject = "Confirmation to reset Account at DocsArchive!";
    String mailText =
        "Hello user "
            + user.get().getUsername()
            + "\n"
            + "You have requested to reset your account.\n "
            + "If you have NOT requested this action, just ignore this message!"
            + "Please click on the below link to reset your DocsArchive account.\n"
            + frontEndDomainUrl
            + "/reset/password/";

    final SimpleMailMessage emailMessage =
        createEmailMessage(email, subject, mailText, resetToken.getId());

    emailService.sendEmail(emailMessage);
  }

  @Override
  public void resetAccountPassword(String resetTokenId, String newPassword) {
    //    verify token exists
    Optional<ResetToken> resetToken = resetTokenService.findTokenById(resetTokenId);
    if (!resetToken.isPresent()) {
      throw new RuntimeException("Token doesnt exists ... Not found page");
    }

    //    verify date ---> less than 1 day else error
    if ( resetToken.get().getCreatedDate() != null && Duration.between(resetToken.get().getCreatedDate(), LocalDate.now()).toDays() >= 1) {
      resetTokenService.deleteResetToken(resetTokenId);
      throw new RuntimeException("Token too old, not vlaid ... Not found page");
    }

    //    change password
    User user = resetToken.get().getUser();
    user.setPassword(newPassword);
    userRepository.save(user);

    //    delete token?
    resetTokenService.deleteResetToken(resetTokenId);
  }

  private SimpleMailMessage createEmailMessage(
      String mailTo, String subject, String text, String token) {
    log.info("Email values:" + "<MAIL>");

    final SimpleMailMessage emailMessage = new SimpleMailMessage();
    emailMessage.setFrom("<MAIL>");
    emailMessage.setTo(mailTo);
    emailMessage.setSubject(subject);
    emailMessage.setText(text + token);

    return emailMessage;
  }

  @Override
  public Optional<User> loadUserByUserName(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public Optional<User> loadUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
