package io.gumga.domain;

import io.gumga.core.GumgaIdable;
import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.*;
import io.gumga.domain.domains.usertypes.*;
import io.gumga.domain.util.UUIDUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class GumgaModelUUID implements GumgaIdable<String>, Serializable {

    @Id
    @Column(name = "id")
    protected String id;

    @Column(name = "oi")
    protected GumgaOi oi;

    public GumgaModelUUID() {
        Class classe = this.getClass();
        if (classe.isAnnotationPresent(GumgaMultitenancy.class) && oi == null) {
            String oc = GumgaThreadScope.organizationCode.get();
            if (oc == null) {
                GumgaMultitenancy tenancy = this.getClass().getAnnotation(GumgaMultitenancy.class);
                oc = tenancy.publicMarking().getMark();
            }
            oi = new GumgaOi(oc);
        }
        this.id = UUIDUtil.generate();
    }

    public GumgaOi getOi() {
        return oi;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOi(GumgaOi oi) {
        this.oi = oi;
    }

//        @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(this);
//    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (id == null) {
            return super.hashCode();
        }
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final GumgaModelUUID other = (GumgaModelUUID) obj;

        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
