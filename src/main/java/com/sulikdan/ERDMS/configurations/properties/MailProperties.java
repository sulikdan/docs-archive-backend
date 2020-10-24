package com.sulikdan.ERDMS.configurations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Daniel Šulik on 24-Oct-20
 * <p>
 * Class MailProperties is used to get properties for mailing.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    private String username;
    private String password;

}
