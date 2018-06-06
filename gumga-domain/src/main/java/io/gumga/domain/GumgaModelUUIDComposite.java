package io.gumga.domain;

import io.gumga.core.GumgaIdable;
import io.gumga.core.GumgaThreadScope;
import io.gumga.domain.domains.GumgaOi;
import io.gumga.domain.util.UUIDUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class GumgaModelUUIDComposite implements GumgaIdable<GumgaModelUUIDCompositePK>, Serializable {

    @EmbeddedId
    protected GumgaModelUUIDCompositePK pk;

    public GumgaModelUUIDComposite() {
        this.pk = new GumgaModelUUIDCompositePK();
        Class classe = this.getClass();
        if (classe.isAnnotationPresent(GumgaMultitenancy.class) && pk.oi == null) {
            String oc = GumgaThreadScope.organizationCode.get();
            if (oc == null) {
                GumgaMultitenancy tenancy = this.getClass().getAnnotation(GumgaMultitenancy.class);
                oc = tenancy.publicMarking().getMark();
            }
            pk.oi = new GumgaOi(oc);
        }
        pk.id = UUIDUtil.generate();
    }

    public GumgaOi getOi() {
        return pk.oi;
    }

    @Override
    public String getId() {
        return pk.id;
    }

    public void setId(String id) {
        pk.id = id;
    }

    public void setOi(GumgaOi oi) {
        pk.oi = oi;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (pk.id == null) {
            return super.hashCode();
        }
        hash = 29 * hash + Objects.hashCode(pk.id);
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

        final GumgaModelUUIDComposite other = (GumgaModelUUIDComposite) obj;

        if (!Objects.equals(pk.id, pk.id)) {
            return false;
        }

        return true;
    }
}
