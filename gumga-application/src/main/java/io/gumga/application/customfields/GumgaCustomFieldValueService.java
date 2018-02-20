package io.gumga.application.customfields;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.customfields.GumgaCustomFieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe para manipulação de valores do {@link GumgaCustomField}
 */
@Service
public class GumgaCustomFieldValueService extends GumgaService<GumgaCustomFieldValue, Long> {

    private final GumgaCustomFieldValueRepository repository;

    @Autowired
    public GumgaCustomFieldValueService(GumgaCustomFieldValueRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Busca valor do campo customizado no objeto
     * @param cf Campo customizado
     * @param obj Objeto que contenha o campo customizado
     * @return
     */
    Object getValue(GumgaCustomField cf, GumgaModel obj) {
        return repository.findByFieldAndGumgaModelId(cf, (Long) obj.getId());
    }

    /**
     * Salva valor no campo customizado do objeto
     * @param newValue Novo valor para o campo customizado
     * @return
     */
    @Override
    public GumgaCustomFieldValue save(GumgaCustomFieldValue newValue) {
        GumgaCustomFieldValue oldValue = repository.findByFieldAndGumgaModelId(newValue.getField(), newValue.getGumgaModelId());
        if (oldValue==null){
            return super.save(newValue); 
        }
        oldValue.setValue(newValue.getValue());
        return super.save(oldValue); 
    }
    
    
}
