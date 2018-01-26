package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaQueryDSLRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends GumgaQueryDSLRepository<Stock, Long> {
}
