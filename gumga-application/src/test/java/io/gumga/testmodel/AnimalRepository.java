package io.gumga.testmodel;

import io.gumga.domain.GumgaModelUUIDComposite;
import io.gumga.domain.GumgaModelUUIDCompositePK;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends GumgaCrudRepository<Animal, GumgaModelUUIDCompositePK> {
}
