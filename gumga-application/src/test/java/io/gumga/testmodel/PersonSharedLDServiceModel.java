package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import io.gumga.domain.logicaldelete.GumgaSharedLDModel;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonSharedLDServiceModel extends GumgaService<PersonSharedLDModel,Long>{

    @Autowired
    public PersonSharedLDServiceModel(GumgaCrudRepository<PersonSharedLDModel, Long> repository) {
        super(repository);
    }


}
