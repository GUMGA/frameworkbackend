package io.gumga.testmodel;

import io.gumga.application.GumgaServiceComposite;
import io.gumga.domain.GumgaModelUUIDCompositePK;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService extends GumgaServiceComposite<Animal, String> {

    @Autowired
    public AnimalService(GumgaCrudRepository<Animal, GumgaModelUUIDCompositePK> repository) {
        super(repository);
    }
}
