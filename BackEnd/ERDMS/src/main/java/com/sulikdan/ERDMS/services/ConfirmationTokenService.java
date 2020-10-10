package com.sulikdan.ERDMS.services;

import com.sulikdan.ERDMS.entities.ConfirmationToken;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class ConfirmationTokenService is used for .....
 */
public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);

    void deleteConfirmationToken(String id);

    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
