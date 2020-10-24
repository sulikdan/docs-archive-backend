package com.sulikdan.ERDMS.services.users;

import com.sulikdan.ERDMS.dto.UserDto;
import com.sulikdan.ERDMS.entities.users.ConfirmationToken;
import com.sulikdan.ERDMS.entities.users.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class UserService is used for work with Users.
 */
public interface UserService extends UserDetailsService {

    void registerUser(User user);

    UserDto registerUser(UserDto userDto);

    void confirmUserRegistration(ConfirmationToken confirmationToken);

    void resetAccount(String email);

    void resetAccountPassword(String resetTokenId, String newPassword);

    Optional<User> loadUserByUserName(String username);

    Optional<User> loadUserByEmail(String email);

}
