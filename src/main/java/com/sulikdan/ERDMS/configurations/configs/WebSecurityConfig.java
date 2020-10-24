package com.sulikdan.ERDMS.configurations.configs;

import com.sulikdan.ERDMS.services.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders
        .AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Daniel Šulik on 11-Oct-20
 *
 * <p>Class WebSecurityconfig is used for .....
 */
@AllArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private UserService jwtUserDetailsService;
  private JwtRequestFilter jwtRequestFilter;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // configure AuthenticationManager so that it knows from where to load
    // user for matching credentials
    // Use BCryptPasswordEncoder
    auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
  }

//  @Bean
//  public BCryptPasswordEncoder bCryptPasswordEncoder() {
//
//    return new BCryptPasswordEncoder();
//  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    // We don't need CSRF for this example
    httpSecurity
        .csrf()
        .disable()
        // dont authenticate this particular request
        .authorizeRequests()
        .antMatchers("/authenticate", "/register", "/api/users/**")
        .permitAll().antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .
        // all other requests need to be authenticated
        anyRequest()
        .authenticated()
        .and()
        .
        // make sure we use stateless session; session won't be used to
        // store user's state.
        exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Add a filter to validate the tokens with every request
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
