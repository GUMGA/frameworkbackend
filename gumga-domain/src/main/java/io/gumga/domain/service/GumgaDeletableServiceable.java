package io.gumga.domain.service;

import java.util.List;

/**
 * Service com a operação de delete
 */
//@FunctionalInterface
public interface GumgaDeletableServiceable<T> {

    public void delete(T resource);
    
    public void delete(List<T> resource);
    

}
