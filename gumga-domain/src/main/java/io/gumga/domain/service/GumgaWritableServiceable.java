package io.gumga.domain.service;

import java.io.Serializable;

/**
 * Service com a operação salvar
 */
public interface GumgaWritableServiceable<T, ID extends Serializable> extends GumgaReadableServiceable<T, ID> {

    public T save(T resource);

}
