package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaQueryDSLRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPlaceRepository extends GumgaQueryDSLRepository<MarketPlace, Long> {
}
