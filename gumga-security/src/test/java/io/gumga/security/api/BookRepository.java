package io.gumga.security.api;

import io.gumga.domain.repository.GumgaCrudRepository;

/**
 * Repositorio de livros
 */
public interface BookRepository extends GumgaCrudRepository<Book,Long> {
}
