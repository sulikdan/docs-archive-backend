package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * @see ConfirmationToken
 */
@Slf4j
@AllArgsConstructor
@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public void deleteConfirmationToken(String id) {
        confirmationTokenRepository.deleteById(id);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findConfirmationTokenByConfirmationToken(token);
    }
}
