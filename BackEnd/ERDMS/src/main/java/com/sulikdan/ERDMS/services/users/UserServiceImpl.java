package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.ConfirmationToken;
import com.sulikdan.ERDMS.entities.User;
import com.sulikdan.ERDMS.repositories.UserRepository;
import com.sulikdan.ERDMS.services.ConfirmationTokenService;
import com.sulikdan.ERDMS.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class UserServiceImpl is used for .....
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final ConfirmationTokenService confirmationTokenService;

  private final EmailService emailService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public void registerUser(User user) {

    String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

    user.setPassword(encryptedPassword);

    User createdUser = userRepository.save(user);

    ConfirmationToken confirmationToken = new ConfirmationToken(user);

    confirmationTokenService.saveConfirmationToken(confirmationToken);

    sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
  }

  @Override
  public void confirmUserRegistration(ConfirmationToken confirmationToken) {

    User user = confirmationToken.getUser();

    user.setEnabled(true);

    userRepository.save(user);

    confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isPresent()) {
      return optionalUser.get();
    } else {
      throw new UsernameNotFoundException(
          MessageFormat.format("User with email {0} cannot be found.", email));
    }
  }

  void sendConfirmationMail(String userMail, String token) {

    final SimpleMailMessage emailMessage = new SimpleMailMessage();
    emailMessage.setFrom("<MAIL>");
    emailMessage.setTo(userMail);
    emailMessage.setSubject("Confirmation link to DocsArchive!");
    emailMessage.setText(
        "Welcome to the DocsArchive!\n"
            + "Thank you for registering.\n "
            + "Please click on the below link to activate your DocsArchive account.\n"
            + "http://localhost:8080/sign-up/confirm?token="
            + token);

    emailService.sendEmail(emailMessage);
  }
}
