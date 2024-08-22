package eu.sulikdan.erdmsbackendv2.service;


import eu.sulikdan.erdmsbackendv2.repo.ETagRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ETagService {
    //TODO delete if unused
    private final ETagRepo eTagRepo;
}
