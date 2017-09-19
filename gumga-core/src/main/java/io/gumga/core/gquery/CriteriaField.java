package io.gumga.core.gquery;

import java.io.Serializable;

public class CriteriaField implements Serializable {
    private String field;

    public CriteriaField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return field;
    }
}
