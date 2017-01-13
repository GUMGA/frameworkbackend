package io.gumga.domain;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Repositório padrão GUMGA
 *
 */
@NoRepositoryBean
public interface GumgaRepository<T, ID extends Serializable> extends Repository<T, ID> {

}
