package eu.sulikdan.erdmsbackendv2.repo;

import eu.sulikdan.erdmsbackendv2.entity.ETag;
import org.springframework.data.repository.CrudRepository;


import java.util.UUID;

public interface ETagRepo extends CrudRepository<UUID, ETag> {


}
