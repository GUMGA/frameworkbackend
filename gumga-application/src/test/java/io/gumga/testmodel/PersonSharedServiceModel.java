package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonSharedServiceModel extends GumgaService<PersonSharedModel,Long>{

    @Autowired
    public PersonSharedServiceModel(GumgaCrudRepository<PersonSharedModel, Long> repository) {
        super(repository);
    }


}
