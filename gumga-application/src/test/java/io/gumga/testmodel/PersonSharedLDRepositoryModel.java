package io.gumga.testmodel;

import io.gumga.domain.logicaldelete.GumgaSharedLDModel;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonSharedLDRepositoryModel extends GumgaCrudRepository<PersonSharedLDModel, Long> {

}
