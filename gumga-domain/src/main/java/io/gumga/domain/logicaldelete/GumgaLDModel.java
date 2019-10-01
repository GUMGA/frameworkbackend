package io.gumga.domain.logicaldelete;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.domains.GumgaOi;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import javax.persistence.Column;

/**
 * Super classe para classes que irão possuir remoção lógica
 *
 * @author munif
 */
@MappedSuperclass
public class GumgaLDModel<ID extends Serializable> extends GumgaModel<ID> implements GumgaLD {

    @Column(name = "gumga_active")
    protected Boolean gumgaActive;

    public GumgaLDModel() {
        super();
        gumgaActive = true;
    }

    public GumgaLDModel(GumgaOi oi) {
        super(oi);
        this.gumgaActive = gumgaActive;
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
