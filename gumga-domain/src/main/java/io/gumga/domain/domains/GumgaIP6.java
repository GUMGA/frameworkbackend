package io.gumga.domain.domains;

import java.util.Objects;

/**
 * Representa um IP6
 *
 * @author munif
 */
public class GumgaIP6 extends GumgaDomain {

    private String value;

    public GumgaIP6() {
    }

    public GumgaIP6(String value) {
        this.value = value;
    }

    public GumgaIP6(GumgaIP6 other) {
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
        final GumgaIP6 other = (GumgaIP6) obj;
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
