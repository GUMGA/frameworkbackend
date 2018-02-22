package io.gumga.testmodel;

import io.gumga.domain.repository.GumgaCrudRepository;

import java.util.List;

/**
 * Repositorio de livros
 */
public interface BookRepository extends GumgaCrudRepository<Book,Long> {

}
