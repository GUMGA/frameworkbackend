package io.gumga.application;

import java.util.Objects;

public class GumgaHqlEntry {

    public final GumgaFieldStereotype type;
    public final String operator;

    public GumgaHqlEntry(GumgaFieldStereotype type, String operator) {
        this.type = type;
        this.operator = operator;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.operator);
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
        final GumgaHqlEntry other = (GumgaHqlEntry) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.operator, other.operator)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HqlEntry{" + "type=" + type + ", operator=" + operator + '}';
    }
}
