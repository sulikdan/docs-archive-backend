package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.repositories.ConfirmationTokenRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConfirmationTokenServiceImplTest {

  @Mock
  ConfirmationTokenRepository confirmationTokenRepository;

  ConfirmationTokenService tokenService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    tokenService = new ConfirmationTokenServiceImpl(confirmationTokenRepository);
  }

  @Test
  void saveConfirmationToken() {
    //given
    ConfirmationToken token = new ConfirmationToken();

    when(confirmationTokenRepository.save(any(ConfirmationToken.class))).thenReturn(token);

    //when
    tokenService.saveConfirmationToken(token);

    //then
    verify(confirmationTokenRepository).save(any(ConfirmationToken.class));
  }

  @Test
  void deleteConfirmationToken() {
    //given
    ConfirmationToken token = new ConfirmationToken();

    doNothing().when(confirmationTokenRepository).deleteById(anyString());

    //when
    tokenService.deleteConfirmationToken(token.getId());

    //then
    verify(confirmationTokenRepository).deleteById(anyString());
  }

  @Test
  void findConfirmationTokenByToken() {
    //given
    ConfirmationToken token = new ConfirmationToken();

    when(confirmationTokenRepository.findConfirmationTokenByConfirmationToken(anyString())).thenReturn(Optional.of(token));

    //when
    Optional<ConfirmationToken> resultTok = tokenService.findConfirmationTokenByToken(token.getId());

    //then
    Assert.assertTrue(resultTok.isPresent());
    Assert.assertEquals(token.getId(), resultTok.get().getId());
    verify(confirmationTokenRepository).findConfirmationTokenByConfirmationToken(anyString());
  }
}
