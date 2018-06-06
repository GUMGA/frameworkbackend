package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaModelUUIDCompositePK;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Service;

@Service
public class AnimalService extends GumgaService<Animal, GumgaModelUUIDCompositePK> {

    public AnimalService(GumgaCrudRepository<Animal, GumgaModelUUIDCompositePK> repository) {
        super(repository);
    }
}
