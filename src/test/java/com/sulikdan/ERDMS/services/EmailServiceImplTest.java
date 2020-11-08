package com.sulikdan.ERDMS.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

  @Mock JavaMailSender javaMailSender;

  EmailService emailService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    emailService = new EmailServiceImpl(javaMailSender);
  }

  @Test
  void sendEmail() {
    // given
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("from@gmail.com");
    mailMessage.setTo("to@gmail.com");
    mailMessage.setText("Ratatat, sending a mail.");

    doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

    // when
    emailService.sendEmail(mailMessage);

    // then
    verify(javaMailSender).send(any(SimpleMailMessage.class));
  }
}
