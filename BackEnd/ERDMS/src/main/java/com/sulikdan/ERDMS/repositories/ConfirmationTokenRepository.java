package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 *
 * <p>Class ConfirmationTokenRepository is repository used for ConfirmationToken.
 */
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {
    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
