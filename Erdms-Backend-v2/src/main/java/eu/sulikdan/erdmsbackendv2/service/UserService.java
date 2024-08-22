package eu.sulikdan.erdmsbackendv2.service;

import eu.sulikdan.erdmsbackendv2.repo.UserRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;
}
