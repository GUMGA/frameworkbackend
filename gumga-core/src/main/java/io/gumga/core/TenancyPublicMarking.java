package io.gumga.core;

/**
 * Marcação do tenancy
 * @author munif
 */
public enum TenancyPublicMarking {

    NULL(null),
    PUBLIC("PUBLIC");
    private final String mark;

    TenancyPublicMarking(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

}
