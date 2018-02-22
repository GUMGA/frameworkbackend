package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonSharedUUIDServiceModel extends GumgaService<PersonSharedUUIDModel, String>{

    @Autowired
    public PersonSharedUUIDServiceModel(GumgaCrudRepository<PersonSharedUUIDModel, String> repository) {
        super(repository);
    }
}
