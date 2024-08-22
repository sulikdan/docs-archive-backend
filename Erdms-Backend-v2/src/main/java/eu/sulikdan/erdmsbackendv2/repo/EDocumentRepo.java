package eu.sulikdan.erdmsbackendv2.repo;

import eu.sulikdan.erdmsbackendv2.entity.EDocument;
import org.springframework.data.repository.CrudRepository;

public interface EDocumentRepo extends CrudRepository<EDocument, Integer> {
}
