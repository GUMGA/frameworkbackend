package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.repository.GumgaQueryDSLRepository;

public interface MyCarRepository extends GumgaCrudRepository<MyCar,Long> {
}
