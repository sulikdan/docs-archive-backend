package com.sulikdan.ERDMS.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class EmailServiceImpl is used for .....
 */
@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Async
  @Override
  public void sendEmail(SimpleMailMessage mailMessage) {
    javaMailSender.send(mailMessage);
  }
}
