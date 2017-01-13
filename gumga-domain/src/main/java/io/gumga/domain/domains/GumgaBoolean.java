/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.domains;

/**
 * Representa um boolean, criado para introduzir os domínios da GUMGA
 *
 * @author munif
 */
public class GumgaBoolean extends GumgaDomain {

    private static final String TRUE_LABEL = "TRUE";
    private static final String FALSE_LABEL = "FALSE";

    private boolean value;

    public GumgaBoolean() {
    }

    public GumgaBoolean(boolean value) {
        this.value = value;
    }

    public GumgaBoolean(GumgaBoolean other) {
        if (other != null) {
            this.value = other.value;
        }
    }

    public boolean is() {
        return value;
    }

    public boolean isValue() {
        return value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.value ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaBoolean other = (GumgaBoolean) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value ? TRUE_LABEL : FALSE_LABEL;
    }

}
