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

/**
 * Classe para manipulação de definições da Tag
 */
@Service
public class GumgaTagDefinitionService extends GumgaService<GumgaTagDefinition, Long> {

    private GumgaTagDefinitionRepository repository;

    @Autowired
    public GumgaTagDefinitionService(GumgaTagDefinitionRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Busca definição de tag de acordo com id
     * @param id Identificador do registro
     * @return Definição de tag
     */
    @Transactional
    public GumgaTagDefinition loadGumgaTagDefinitionFat(Long id) {
        GumgaTagDefinition obj = repository.findOne(id);
        Hibernate.initialize(obj.getAttributes());
        return obj;
    }

    /**
     * Novo objeto de definição de tag com determinado nome
     * @param name Nome da definição
     * @return Definição de tag
     */
    public GumgaTagDefinition createNew(String name){
        return new GumgaTagDefinition(name);
    }

    /**
     * Novo objeto de definição de tag com determinado nome e com atributos
     * @param name Nome da definição
     * @return Definição de tag
     */
    public GumgaTagDefinition createNew(String name, String... attributes){
        GumgaTagDefinition def = new GumgaTagDefinition(name);
        def.addAttributes(attributes);
        return def;
    }

    /**
     * Pesquisa de definições de tags
     * @param query Objeto de pesquisa
     * @return Resultado da pesquisa
     */
    @Override
    public SearchResult<GumgaTagDefinition> pesquisa(QueryObject query) {
        return repository.search(new QueryObjectLikeDecorator(query).build());
    }
    
}
