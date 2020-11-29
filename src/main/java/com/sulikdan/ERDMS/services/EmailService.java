package com.sulikdan.ERDMS.services;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class EmailService is used for emailing
 */
public interface EmailService {
    void sendEmail(SimpleMailMessage mailMessage);
}
