package io.gumga.core.gquery;

import java.io.Serializable;

public enum JoinType implements Serializable {
    INNER(" inner join "),
    LEFT(" left join ");

    private final String name;
    JoinType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
