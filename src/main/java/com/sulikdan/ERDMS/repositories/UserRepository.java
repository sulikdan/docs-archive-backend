package com.sulikdan.ERDMS.repositories;

import com.sulikdan.ERDMS.entities.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by Daniel Šulik on 10-Oct-20
 * <p>
 * Class UserRepository is used to work with DB collection users.
 */
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
