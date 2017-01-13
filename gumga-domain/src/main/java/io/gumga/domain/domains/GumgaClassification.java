package io.gumga.domain.domains;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Tipo para classificação de registros em hierarquia.
 */
public class GumgaClassification implements Comparable<GumgaClassification>, Serializable {

    private static final int CLASSIFICATION_PART_SIZE = 3;
    private static final String CLASSIFICATION_REGEX = "^[0-9]{" + CLASSIFICATION_PART_SIZE + "}(\\.[0-9]{" + CLASSIFICATION_PART_SIZE + "})*$";

    private static final long serialVersionUID = 1L;
    private String value;

    private GumgaClassification(String value) {
        Preconditions.checkArgument(value.matches(CLASSIFICATION_REGEX));
        this.value = value;
    }

    public String toString() {
        return this.value;
    }

    public boolean isRoot() {
        return !this.value.contains(".");
    }

    public GumgaClassification getParent() {
        if (!isRoot()) {
            return new GumgaClassification(this.value.substring(0, this.value.lastIndexOf('.')));
        }
        return null;
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || !GumgaClassification.class.isInstance(obj)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        return this.value.equals(GumgaClassification.class.cast(obj).value);
    }

    public GumgaClassification next() {
        String index = this.value.substring(this.value.lastIndexOf('.') + 1);
        String nextIndex = Long.valueOf(Long.valueOf(index) + 1).toString();
        nextIndex = Strings.padStart(nextIndex, CLASSIFICATION_PART_SIZE, '0');

        if (!isRoot()) {
            nextIndex = MessageFormat.format("{0}.{1}", this.getParent(), nextIndex);
        }

        return new GumgaClassification(nextIndex);
    }

    @Override
    public int compareTo(GumgaClassification other) {
        return this.value.compareTo(other.value);
    }

    public static GumgaClassification fromString(String value) {
        return new GumgaClassification(value);
    }

    public long getLevel() {
        return this.value.split("\\.").length;
    }

    public boolean isChildOf(GumgaClassification another) {
        return this.value.startsWith(another.value);
    }
}
