package io.gumga.domain;

import io.gumga.domain.domains.GumgaOi;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GumgaModelUUIDCompositePK implements Serializable {

    @Column(name = "id")
    protected String id;
    @Column(name = "oi")
    protected GumgaOi oi;

    protected GumgaModelUUIDCompositePK() {}

    public GumgaModelUUIDCompositePK(String id, GumgaOi oi) {
        this.id = id;
        this.oi = oi;
    }

    public GumgaModelUUIDCompositePK(String id) {
        this.id = id;
        this.oi = GumgaOi.getCurrentOi();
    }

    public String getId() {
        return id;
    }

    public GumgaOi getOi() {
        return oi;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
