package io.gumga.domain.customfields;

import io.gumga.domain.GumgaModel;
import io.gumga.domain.GumgaMultitenancy;

import javax.persistence.*;
import java.util.Objects;

/**
 * Representa um atributo genérico
 *
 * @author munif
 */
@Entity
@SequenceGenerator(name = GumgaModel.SEQ_NAME, sequenceName = "SEQ_GUMGA_CTM_FLD")
//             123456789012345678
@Table(name = "gumga_ctm_fld")
@GumgaMultitenancy
public class GumgaCustomField extends GumgaModel<Long> {

    private String clazz;
    private String name;
    private String description;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private CustomFieldType type;
    private String validationDescription;
    private String validationScript;
    private String defaultValueScript;
    private String options;
    private String optionValueField;
    private String optionLabelField;
    private String optionsCollection;
    private Double visualizationOrder;
    private String fieldGroup;
    private String translateKey;

    public GumgaCustomField() {
        this.name = "";
        this.description = "";
        this.active = true;
        this.type = CustomFieldType.TEXT;
        this.validationDescription = "";
        this.validationScript = "true";
        this.defaultValueScript = "''";
        this.options = null;
        this.visualizationOrder = 10.0;
        this.fieldGroup = "$DEFAULT";
        this.translateKey = "class.custom_field";
    }

    public GumgaCustomField(String clazz, String name, String description, Boolean active, CustomFieldType type, String validationDescription, String validationScript, String defaultValueScript, String options, Double visualizationOrder, String fieldGroup, String translateKey) {
        this.clazz = clazz;
        this.name = name;
        this.description = description;
        this.active = active;
        this.type = type;
        this.validationDescription = validationDescription;
        this.validationScript = validationScript;
        this.defaultValueScript = defaultValueScript;
        this.options = options;
        this.visualizationOrder = visualizationOrder;
        this.fieldGroup = fieldGroup;
        this.translateKey = translateKey;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CustomFieldType getType() {
        return type;
    }

    public void setType(CustomFieldType type) {
        this.type = type;
    }

    public String getValidationDescription() {
        return validationDescription;
    }

    public void setValidationDescription(String validationDescription) {
        this.validationDescription = validationDescription;
    }

    public String getValidationScript() {
        return validationScript;
    }

    public void setValidationScript(String validationScript) {
        this.validationScript = validationScript;
    }

    public String getDefaultValueScript() {
        return defaultValueScript;
    }

    public void setDefaultValueScript(String defaultValueScript) {
        this.defaultValueScript = defaultValueScript;
    }

    public Double getVisualizationOrder() {
        return visualizationOrder;
    }

    public void setVisualizationOrder(Double visualizationOrder) {
        this.visualizationOrder = visualizationOrder;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getFieldGroup() {
        return fieldGroup;
    }

    public void setFieldGroup(String fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    public String getTranslateKey() {
        return translateKey;
    }

    public void setTranslateKey(String translateKey) {
        this.translateKey = translateKey;
    }

    public String getOptionValueField() {
        return optionValueField;
    }

    public void setOptionValueField(String optionValueField) {
        this.optionValueField = optionValueField;
    }

    public String getOptionLabelField() {
        return optionLabelField;
    }

    public void setOptionLabelField(String optionLabelField) {
        this.optionLabelField = optionLabelField;
    }

    public String getOptionsCollection() {
        return optionsCollection;
    }

    public void setOptionsCollection(String optionsCollection) {
        this.optionsCollection = optionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.clazz);
        hash = 89 * hash + Objects.hashCode(this.name);
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
        final GumgaCustomField other = (GumgaCustomField) obj;
        if (!Objects.equals(this.clazz, other.clazz)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
