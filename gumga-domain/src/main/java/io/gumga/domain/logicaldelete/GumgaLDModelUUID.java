package io.gumga.domain.logicaldelete;

import io.gumga.domain.GumgaModelUUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class GumgaLDModelUUID extends GumgaModelUUID implements GumgaLD {

    @Column(name = "gumga_active")
    protected Boolean gumgaActive;

    public GumgaLDModelUUID() {
        super();
        gumgaActive = true;
    }

    @Override
    public Boolean getGumgaActive() {
        return gumgaActive;
    }

    @Override
    public void setGumgaActive(Boolean gumgaActive) {
        this.gumgaActive = gumgaActive;
    }
}
