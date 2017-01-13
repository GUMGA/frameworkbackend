package io.gumga.domain.tag;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Representa a definição de um valor para TAGs
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GTAG_VALU_DEFI")
@Table(name = "gtag_vdefi")
@GumgaMultitenancy
public class GumgaTagValueDefinition extends GumgaModel<Long> {

    @Version
    private Integer version;

    @NotNull
    private String name;

    public GumgaTagValueDefinition() {
    }

    public GumgaTagValueDefinition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
