package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonServiceModel extends GumgaService<PersonModel,Long>{

    @Autowired
    public PersonServiceModel(GumgaCrudRepository<PersonModel, Long> repository) {
        super(repository);
    }


}
