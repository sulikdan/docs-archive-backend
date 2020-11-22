package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulikdan.ERDMS.configurations.configs.JwtTokenUtil;
import com.sulikdan.ERDMS.dto.UserDto;
import com.sulikdan.ERDMS.entities.jwt.JwtRequest;
import com.sulikdan.ERDMS.entities.jwt.JwtResponse;
import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.entities.users.ResetToken;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.services.users.ConfirmationTokenService;
import com.sulikdan.ERDMS.services.users.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock UserService userService;
  @Mock ConfirmationTokenService confirmationTokenService;
  @Mock AuthenticationManager authenticationManager;
  @Mock JwtTokenUtil jwtTokenUtil;
  ObjectMapper mapper;

  @InjectMocks UserController controller;

  com.sulikdan.ERDMS.entities.users.User user;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    user = new User();
    user.setUsername("matrix");
    user.setPassword("matrix");
    user.setEmail("yolo@gmail.com");

    mapper = new ObjectMapper();
  }

  @Test
  void createAuthenticationToken() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("matrix", "matrix123");
    final String jwtToken = "RandomJWT-Token-Top_kek";
    final JwtResponse jwtResponse = new JwtResponse(jwtToken);

    //
    // when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(new Aut);
    when(userService.loadUserByUsername(anyString())).thenReturn(user);
    when(jwtTokenUtil.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

    // when
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/users/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(jwtRequest)))
            .andExpect(status().isOk())
            .andReturn();

    //     then
    Assert.assertEquals(
        mapper.writeValueAsString(jwtResponse), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void saveUser() throws Exception {
    //    userService = mock(UserService.class, RETURNS_DEEP_STUBS);

    UserDto userDto = new UserDto();
    userDto.setUsername("matrix");
    userDto.setPassword("matrix");
    userDto.setEmail("yolo@gmail.com");

    //    JwtRequest jwtRequest = new JwtRequest("matrix", "matrix123");
    //    final String token = "RandomJWT-Token-Top_kek";

    //    when(userService.loadUserByUserName(any()).isPresent()).thenReturn(false);
    //    when(userService.loadUserByEmail(any()).isPresent()).thenReturn(false);
    when(userService.loadUserByUserName(any())).thenReturn(Optional.empty());
    when(userService.loadUserByEmail(any())).thenReturn(Optional.empty());

    when(userService.registerUser(any(UserDto.class))).thenReturn(userDto);
    //    doReturn(userDto).when(userDetailsService.registerUser(any(UserDto.class)));

    // when
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(userDto)))
            .andExpect(status().isOk())
            .andReturn();

    // then
    Assert.assertEquals(
        mapper.writeValueAsString(userDto), mvcResult.getResponse().getContentAsString());
  }

  @Test
  void confirmMail() throws Exception {

    UserDto userDto = new UserDto();
    userDto.setUsername("matrix");
    userDto.setPassword("matrix");
    userDto.setEmail("yolo@gmail.com");

    //    JwtRequest jwtRequest = new JwtRequest("matrix", "matrix123");
    final String token = "RandomJWT-Token-Top_kek";
    ConfirmationToken confirmationToken = new ConfirmationToken(user);
    confirmationToken.setConfirmationToken(token);

    when(confirmationTokenService.findConfirmationTokenByToken(anyString()))
        .thenReturn(Optional.of(confirmationToken));
    doNothing().when(userService).confirmUserRegistration(confirmationToken);

    // when

    this.mockMvc
        .perform(post("/users/register/confirm?token={}", token).requestAttr("token", token))
        .andExpect(status().isOk());

    // then
    verify(confirmationTokenService).findConfirmationTokenByToken(anyString());
    verify(userService).confirmUserRegistration(any());
  }

  @Test
  void resetUser() throws Exception {
    // given

    doNothing().when(userService).resetAccount(anyString());

    // when
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/users/resetAccount")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"email\" : \"yolo@gmail.com\"}"))
            .andExpect(status().isOk())
            .andReturn();

    // then
    Assert.assertEquals(
        mapper.writeValueAsString("An confirmation e-mail was sent to your e-mail."),
        mvcResult.getResponse().getContentAsString());
    verify(userService).resetAccount(anyString());
  }

  @Test
  void resetPassword() throws Exception {
    // given
    ResetToken resetToken = new ResetToken(user);

    doNothing().when(userService).resetAccountPassword(anyString(), anyString());

    // when
    MvcResult mvcResult =
        this.mockMvc
            .perform(
                post("/users/resetAccount/{}", resetToken.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"password\" : \"matrix\"}"))
            .andExpect(status().isOk())
            .andReturn();

    // then
    Assert.assertEquals(
        mapper.writeValueAsString("The account password was reset!"), mvcResult.getResponse().getContentAsString());
    verify(userService).resetAccountPassword(anyString(), anyString());
  }
}
