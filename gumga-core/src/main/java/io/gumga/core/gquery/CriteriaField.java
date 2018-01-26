package io.gumga.core.gquery;

import java.io.Serializable;

/**
 * Campo utilizado em critérios de busca (comparação de valores de um campo com outro)
 */
public class CriteriaField implements Serializable {
    private String field;

    public CriteriaField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return field;
    }
}
