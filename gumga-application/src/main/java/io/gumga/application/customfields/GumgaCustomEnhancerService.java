package io.gumga.application.customfields;

import io.gumga.core.GumgaIdable;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.customfields.GumgaCustomFieldValue;
import io.gumga.domain.customfields.GumgaCustomizableModel;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Classe auxiliar para tratamento dos campos customizados
 * @author munif
 */
@Service
public class GumgaCustomEnhancerService {

    @Autowired
    private GumgaCustomFieldService customFieldService;

    @Autowired
    private GumgaCustomFieldValueService customFieldValueService;

    /**
     *
     * @param object
     */
    public void setDefaultValues(Object object) {
        if (!(object instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) object;
        List<GumgaCustomField> customFields = loadAllCustomFields(gumgaCustomizable.getClass());
        for (GumgaCustomField cf : customFields) {
            GumgaCustomFieldValue newValue = newValue(cf);
            gumgaCustomizable.getGumgaCustomFields().put(cf.getName(), newValue);
        }
    }

    /**
     * Criar uma nova instancia de um attributo generico
     * @param cf
     * @return
     */
    public GumgaCustomFieldValue newValue(GumgaCustomField cf) {
        return new GumgaCustomFieldValue(cf);
    }

    /**
     * Carregar os attributos genericos do objeto
     * @param gumgaModel
     */
    public void loadCustomFields(Object gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        List<GumgaCustomField> customFields = loadAllCustomFields(gumgaModel.getClass());
        for (GumgaCustomField cf : customFields) {
            Object value = customFieldValueService.getValue(cf, (GumgaModel) gumgaModel);
            if (value == null) {
                value = newValue(cf);
            }
            gumgaCustomizable.getGumgaCustomFields().put(cf.getName(), value);
        }
    }

    public List<GumgaCustomField> loadAllCustomFields(Class gumgaModelClass) {
        List<GumgaCustomField> customFields=new ArrayList<>();
        if (!gumgaModelClass.getSuperclass().equals(GumgaCustomizableModel.class)){
            customFields.addAll(loadAllCustomFields(gumgaModelClass.getSuperclass()));
        }
        customFields.addAll(customFieldService.findByClass(gumgaModelClass));
        return customFields;
    }

    /**
     * Salvar atributos genericos
     * @param gumgaModel
     */
    public void saveCustomFields(GumgaIdable gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        Set<String> keySet = gumgaCustomizable.getGumgaCustomFields().keySet();
        for (String s : keySet) {
            GumgaCustomFieldValue value = (GumgaCustomFieldValue) gumgaCustomizable.getGumgaCustomFields().get(s);
            if (value != null) {
                value.setGumgaModelId((Long) gumgaModel.getId());
                customFieldValueService.save(value);
            }
        }

    }

    /**
     * Remover atributos genericos
     * @param gumgaModel
     */
    public void deleteCustomFields(GumgaIdable gumgaModel) {
        if (!(gumgaModel instanceof GumgaCustomizableModel)) {
            return;
        }
        GumgaCustomizableModel gumgaCustomizable = (GumgaCustomizableModel) gumgaModel;
        Set<String> keySet = gumgaCustomizable.getGumgaCustomFields().keySet();
        for (String s : keySet) {
            GumgaCustomFieldValue value = (GumgaCustomFieldValue) gumgaCustomizable.getGumgaCustomFields().get(s);
            if (value != null) {
                customFieldValueService.delete(value);
            }
        }

    }

}
