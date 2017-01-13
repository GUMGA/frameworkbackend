package io.gumga.domain.service;

/**
 * Service com a operação de delete
 */
@FunctionalInterface
public interface GumgaDeletableServiceable<T> {

    public void delete(T resource);

}
