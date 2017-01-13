package io.gumga.application;

import io.gumga.domain.repository.GumgaQueryDSLRepository;

/**
 * Repositorio de carros
 */
public interface CarRepository extends GumgaQueryDSLRepository<Car,Long> {
}
