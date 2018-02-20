package io.gumga.domain.service;

import java.io.Serializable;
import java.util.List;

/**
 * Service com a operação de delete
 */
//@FunctionalInterface
public interface GumgaDeletableServiceable<T, ID extends Serializable> {

    void delete(T resource);
    
    void delete(List<T> resource);

    void deletePermanentGumgaLDModel(T entity);
    void deletePermanentGumgaLDModel(ID id);
    

}
