package io.gumga.domain.domains;

import com.google.common.base.Objects;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.TenancyPublicMarking;

/**
 * Representa o código organizacional, fundamental para o Multitenancy.
 *
 * @author munif
 */
public class GumgaOi extends GumgaDomain implements Comparable<GumgaOi> {

    private String value;

    protected GumgaOi() {

    }

    public GumgaOi(String value) {
        this.value = value;
    }

    public GumgaOi(GumgaOi other) {
        if (other != null) {
            this.value = other.value;
        }

    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GumgaOi that = (GumgaOi) o;
        return Objects.equal(this.value, that.value);
    }

    @Override
    public int compareTo(GumgaOi o) {
        if (o == null || o.value == null) {
            return (this.value == null) ? 0 : 1;
        }

        if (this.value == null) {
            return -1;
        }

        return this.value.compareTo(o.value);
    }

    public static GumgaOi getCurrentOi() {
        GumgaOi oi = new GumgaOi();
        oi.value = GumgaThreadScope.organizationCode.get();
        return oi;
    }

    public static GumgaOi getParentOi(GumgaOi oi) {
        String oiValue = oi.getValue();
        oiValue = oiValue.substring(0, oiValue.lastIndexOf('.'));
        if (oiValue.lastIndexOf('.') == -1) {
            return null;
        }
        oiValue = oiValue.substring(0, oiValue.lastIndexOf('.')) + ".";
        GumgaOi parent = new GumgaOi(oiValue);
        return parent;
    }

    public static final GumgaOi MARK_PUBLIC = new GumgaOi(TenancyPublicMarking.PUBLIC.getMark());
    public static final GumgaOi MARK_NULL = new GumgaOi(TenancyPublicMarking.NULL.getMark());

    public static void main(String args[]) {
        GumgaOi oi = new GumgaOi("1.2.3.4.5.");
        while (oi != null) {
            System.out.println("---->" + oi);
            oi = GumgaOi.getParentOi(oi);
        }

    }

}
