package io.gumga.domain.tag;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um tag genérica
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_GTAG")
@Table(name = "gumga_gtag")
@GumgaMultitenancy
public class GumgaTag extends GumgaModel<Long> {

    @Version
    private Integer version;

    @NotNull
    private String objectType;

    @NotNull
    private Long objectId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "g_values")
    private List<GumgaTagValue> values = new ArrayList<>();

    @ManyToOne
    private GumgaTagDefinition definition;

    public GumgaTag() {
    }

    public GumgaTag(GumgaTagDefinition definition) {
        this.definition = definition;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public List<GumgaTagValue> getValues() {
        return values;
    }

    public void setValues(List<GumgaTagValue> values) {
        this.values = values;
    }

    public GumgaTagDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(GumgaTagDefinition definition) {
        this.definition = definition;
    }

    public void addValues(GumgaTagValueDefinition valDef, String... values) {
        this.values = new ArrayList<>();
        for (String value : values) {
            addValue(valDef, value);
        }
    }

    public void addValue(GumgaTagValueDefinition valDef, String value) {
        this.values.add(new GumgaTagValue(valDef, value));
    }

    @Override
    public String toString() {
        return "GumgaTag{" + "version=" + version + ", objectType=" + objectType + ", objectId=" + objectId + ", values=" + values + ", definition=" + definition + '}';
    }

}
