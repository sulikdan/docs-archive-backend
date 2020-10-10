package com.sulikdan.ERDMS.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class WebConfiguration is used for .....
 */
@Configuration
public class WebConfiguration {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();
  }
}
