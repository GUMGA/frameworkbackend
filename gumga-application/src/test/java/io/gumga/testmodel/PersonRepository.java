package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GumgaCrudRepository<Person, String> {
}
