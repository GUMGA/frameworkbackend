package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonSharedUUIDRepositoryModel extends GumgaCrudRepository<PersonSharedUUIDModel, String> {

}
