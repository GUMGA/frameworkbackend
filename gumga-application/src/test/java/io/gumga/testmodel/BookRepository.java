package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;

/**
 * Repositorio de livros
 */
public interface BookRepository extends GumgaCrudRepository<Book,Long> {
}
