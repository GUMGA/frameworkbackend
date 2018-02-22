package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonRepositoryModel extends GumgaCrudRepository<PersonModel, Long> {

}
