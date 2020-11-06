package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.entities.users.ResetToken;
import com.sulikdan.ERDMS.repositories.ResetTokenRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ResetTokenServiceImplTest {

    @Mock
    ResetTokenRepository resetTokenRepository;

    ResetTokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        tokenService = new ResetTokenServiceImpl(resetTokenRepository);
    }

    @Test
    void saveResetToken() {
        //given
        ResetToken token = new ResetToken();

        when(resetTokenRepository.save(any(ResetToken.class))).thenReturn(token);

        //when
        tokenService.saveResetToken(token);

        //then
        verify(resetTokenRepository).save(any(ResetToken.class));
    }

    @Test
    void deleteResetToken() {
        //given
        ResetToken token = new ResetToken();

        doNothing().when(resetTokenRepository).deleteById(anyString());

        //when
        tokenService.deleteResetToken(token.getId());

        //then
        verify(resetTokenRepository).deleteById(anyString());
    }

    @Test
    void findTokenById() {
        //given
        ResetToken token = new ResetToken();

        when(resetTokenRepository.findById(anyString())).thenReturn(Optional.of(token));

        //when
        Optional<ResetToken> resultTok = tokenService.findTokenById(token.getId());

        //then
        Assert.assertTrue(resultTok.isPresent());
        Assert.assertEquals(token.getId(), resultTok.get().getId());
        verify(resetTokenRepository).findById(anyString());
    }
}
