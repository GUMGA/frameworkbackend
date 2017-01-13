package io.gumga.domain.customfields;

import io.gumga.domain.GumgaModel;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Super classe para classe que irão possuir atributos genéricos
 *
 * @author munif
 */
@MappedSuperclass
public class GumgaCustomizableModel<ID extends Serializable> extends GumgaModel<ID> {

    @Transient
    private final Map<String, GumgaCustomFieldValue> gumgaCustomFields;

    public GumgaCustomizableModel() {
        super();
        gumgaCustomFields = new LinkedHashMap<>();
    }

    public Map<String, GumgaCustomFieldValue> getGumgaCustomFields() {
        return gumgaCustomFields;
    }

}
