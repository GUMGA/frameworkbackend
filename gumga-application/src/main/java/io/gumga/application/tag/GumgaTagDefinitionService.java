package io.gumga.application.tag;

import io.gumga.application.GumgaService;
import io.gumga.application.QueryObjectLikeDecorator;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.tag.GumgaTagDefinition;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GumgaTagDefinitionService extends GumgaService<GumgaTagDefinition, Long> {

    private GumgaTagDefinitionRepository repository;

    @Autowired
    public GumgaTagDefinitionService(GumgaTagDefinitionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Transactional
    public GumgaTagDefinition loadGumgaTagDefinitionFat(Long id) {
        GumgaTagDefinition obj = repository.findOne(id);
        Hibernate.initialize(obj.getAttributes());
        return obj;
    }
    
    public GumgaTagDefinition createNew(String name){
        return new GumgaTagDefinition(name);
    }

    public GumgaTagDefinition createNew(String name, String... attributes){
        GumgaTagDefinition def = new GumgaTagDefinition(name);
        def.addAttributes(attributes);
        return def;
    }
    
    
    @Override
    public SearchResult<GumgaTagDefinition> pesquisa(QueryObject query) {
        return repository.search(new QueryObjectLikeDecorator(query).build());
    }
    
}
