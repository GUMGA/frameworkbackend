package io.gumga.application.customfields;

import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.repository.GumgaCrudRepository;

import java.util.List;

public interface GumgaCustomFieldRepository extends GumgaCrudRepository<GumgaCustomField, Long> {

    /**
     * Retonar a lista de atributos genericos da classe passada por parametro
     * @param clazz nome da classe
     * @return
     */
    List<GumgaCustomField> findByClazz(String clazz);

}
