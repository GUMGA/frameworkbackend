package io.gumga.application.tag;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.domain.tag.GumgaTagDefinition;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Classe para manipulação de tags do registro
 */
@Service
public class GumgaTagService extends GumgaService<GumgaTag, Long> {

    private GumgaTagRepository repository;

    @Autowired
    public GumgaTagService(GumgaTagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * Carrega tag de acordo com o Id
     * @param id Identificador da tag
     * @return Tag
     */
    @Transactional
    public GumgaTag loadGumgaTagFat(Long id) {
        GumgaTag obj = repository.getOne(id);
        Hibernate.initialize(obj.getValues());
        return obj;
    }

    /**
     * Cria Tag de acordo com uma definição para determinado tipo de objeto
     * @param tagDef Definição de tag
     * @param objectType Tipo do objeto
     * @param objectId Id do objeto
     * @return Tag
     */
    public GumgaTag createNew(GumgaTagDefinition tagDef, String objectType, Long objectId) {
        GumgaTag tag = new GumgaTag(tagDef);
        tag.setObjectType(objectType);
        tag.setObjectId(objectId);
        return tag;
    }

    /**
     * Busca tags com determinado tipo e id do objeto
     * @param objectType Tipo do objeto
     * @param objectId Id do objeto
     * @return Lista de Tags
     */
    @Transactional
    public List<GumgaTag> findByObjectTypeAndObjectId(String objectType, Long objectId) {
        QueryObject qo = new QueryObject();
        qo.setAq("obj.objectType='" + objectType + "' AND obj.objectId=" + objectId);
        return repository.search(qo).getValues();
    }

    /**
     * Busca tags de acordo com o objeto que contenha a mesma
     * @param obj Objeto que contenha a tag
     * @return Lista de Tags
     */
    @Transactional
    public List<GumgaTag> findByObject(GumgaIdable<Long> obj) {
        return findByObjectTypeAndObjectId(obj.getClass().getName(), obj.getId());
    }

}
