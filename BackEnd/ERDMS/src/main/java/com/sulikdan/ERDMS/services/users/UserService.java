package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.entities.ConfirmationToken;
import com.sulikdan.ERDMS.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class UserService is used for .....
 */
public interface UserService extends UserDetailsService {

    void registerUser(User user);

    void confirmUserRegistration(ConfirmationToken confirmationToken);

}
