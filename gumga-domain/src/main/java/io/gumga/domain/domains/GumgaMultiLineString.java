/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain.domains;

import java.util.Objects;

/**
 * Representa um String, mas que quando utilizado irá gerar uma caixa de texto
 * com multilinhas
 *
 * @author munif
 */
public class GumgaMultiLineString extends GumgaDomain {

    private String value;

    public GumgaMultiLineString() {
    }

    public GumgaMultiLineString(String value) {
        this.value = value;
    }

    public GumgaMultiLineString(GumgaMultiLineString other) {
        if (other != null) {
            this.value = other.value;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.value);
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
        final GumgaMultiLineString other = (GumgaMultiLineString) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

}
