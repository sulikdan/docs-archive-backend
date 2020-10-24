package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.users.ResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Daniel Šulik on 18-Oct-20
 *
 * <p>Class ResetTokenRepository is used to work with DB collection ResetToken.
 */
public interface ResetTokenRepository extends MongoRepository<ResetToken, String> {}
