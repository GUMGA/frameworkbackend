package io.gumga.security;

import io.gumga.domain.GumgaModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;

/**
 * DTO para operação
 */
public class GumgaOperationTO implements Serializable {

    /**
     * Nome da operação
     */
    public String name;

    /**
     * Descrição
     */
    public String description;

    /**
     * Identificador da operação
     */
    public String key;

    /**
     * Operação básica ou padrão
     */
    public Boolean basicOperation;

    /**
     * Operação bilhetada
     */
    public Boolean billed;

    /**
     * Valor a cada mil requisições
     */
    public BigDecimal thousandValue;

    public GumgaOperationTO(String key, boolean basic) {
        this.key = key;
        this.description = key;
        this.name = key;
        this.basicOperation = basic;
    }

    public GumgaOperationTO(GumgaOperationKey gok) {
        key = gok.value();
        description = gok.value();
        name = gok.value();
        basicOperation = gok.basic();
    }

    @Override
    public String toString() {
        return "GumgaOperationTO{" + "key=" + key + ", basicOperation=" + basicOperation + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GumgaOperationTO other = (GumgaOperationTO) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

}
