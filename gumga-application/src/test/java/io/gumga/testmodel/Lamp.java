package io.gumga.testmodel;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.domains.GumgaBoolean;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_LAMP")
@GumgaMultitenancy
public class Lamp extends GumgaModel<Long> {

    @Version
    private Integer version;

    private String description;

    private GumgaBoolean ison;

    public Lamp(String description, GumgaBoolean ison) {
        this.description = description;
        this.ison = ison;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GumgaBoolean getIson() {
        return ison;
    }

    public void setIson(GumgaBoolean ison) {
        this.ison = ison;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Lamp{" + "version=" + version + ", description=" + description + ", on=" + ison + '}';
    }

}
