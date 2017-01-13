package io.gumga.application.customfields;

import io.gumga.application.GumgaService;
import io.gumga.domain.GumgaModel;
import io.gumga.domain.customfields.GumgaCustomField;
import io.gumga.domain.customfields.GumgaCustomFieldValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaCustomFieldValueService extends GumgaService<GumgaCustomFieldValue, Long> {

    private final GumgaCustomFieldValueRepository repository;

    @Autowired
    public GumgaCustomFieldValueService(GumgaCustomFieldValueRepository repository) {
        super(repository);
        this.repository = repository;
    }

    Object getValue(GumgaCustomField cf, GumgaModel obj) {
        return repository.findByFieldAndGumgaModelId(cf, (Long) obj.getId());
    }

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
