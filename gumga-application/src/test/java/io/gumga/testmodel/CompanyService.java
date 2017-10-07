package io.gumga.testmodel;

import io.gumga.application.GumgaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends GumgaService<Company, Long> {

    @Autowired
    public CompanyService(CompanyRepository repository) {
        super(repository);
    }

    @Override
    public void afterSave(Company entity) {
        
    }

    @Override
    public void afterUpdate(Company entity) {
        
    }

    public long count() {
        return repository.count();
    }

}
