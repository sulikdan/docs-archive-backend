package eu.sulikdan.erdmsbackendv2.repo;

import eu.sulikdan.erdmsbackendv2.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Integer> {
}
