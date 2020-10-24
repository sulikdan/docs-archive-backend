package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.ConfirmationToken;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class ConfirmationTokenService is used for work with Confirmation Tokens generated while creating an account for
 * user.
 */
public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationToken confirmationToken);

    void deleteConfirmationToken(String id);

    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
