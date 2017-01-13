package io.gumga.domain.domains;

import java.util.Objects;

/**
 * Representa uma URL genérica
 *
 * @author munif
 */
public class GumgaURL extends GumgaDomain {

    private String value;

    public GumgaURL() {
    }

    public GumgaURL(String value) {
        this.value = value;
    }

    public GumgaURL(GumgaURL other) {
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
        final GumgaURL other = (GumgaURL) obj;
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
