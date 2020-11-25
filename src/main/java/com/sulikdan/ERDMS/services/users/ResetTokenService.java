package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.entities.users.ResetToken;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 19-Oct-20
 * <p>
 * Class ResetTokenService is used to work with Reset Tokens intended for user, when they forget they password or
 * username.
 */
public interface ResetTokenService {

    void saveResetToken(ResetToken resetToken);

    void deleteResetToken(String id);

    Optional<ResetToken> findTokenById(String id);

}
