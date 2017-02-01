package io.gumga.application.mongo;

import io.gumga.domain.domains.GumgaOi;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

public abstract class GumgaMongoModel implements Serializable {

    @Id
    protected String id;

    protected GumgaOi oi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GumgaOi getOi() {
        return oi;
    }

    public void setOi(GumgaOi oi) {
        this.oi = oi;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final GumgaMongoModel other = (GumgaMongoModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
