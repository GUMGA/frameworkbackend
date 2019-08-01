package io.gumga.presentation.api;

import io.gumga.annotations.GumgaSwagger;
import io.gumga.application.GumgaUserDataService;
import io.gumga.application.tag.GumgaTagDefinitionService;
import io.gumga.application.tag.GumgaTagService;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.QueryToSave;
import io.gumga.core.SearchResult;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaServiceable;
import io.gumga.domain.GumgaUserData;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.domain.tag.GumgaTagDefinition;
import io.swagger.annotations.ApiOperation;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API para acesso aos métodos de serviços de somente leitura que o Framework dispõe via requisições REST
 * @param <T>
 * @param <ID>
 */
@RestController
public abstract class AbstractReadOnlyGumgaAPI<T, ID extends Serializable> extends AbstractProtoGumgaAPI<T, ID> {

    protected GumgaReadableServiceable<T, ID> service;
    @Autowired
    protected GumgaUserDataService guds;
    @Autowired
    protected GumgaTagDefinitionService gtds;
    @Autowired
    protected GumgaTagService gts;

    /**
     *  * @param service
     */
    public AbstractReadOnlyGumgaAPI(GumgaReadableServiceable<T, ID> service) {
        this.service = service;
    }

    /**
     * Faz uma pesquisa pela query informada através do objeto QueryObjet, cujos atributos são aq, q, start, pageSize, sortField, sortDir e searchFields. Além disso, possibilita filtar os atributos na saída através do parâmetro gumgaFields no header.
     * @param request Objeto HttpServletRequest contendo os parâmetros de HTTP
     * @param query Objeto QueryObject contendo os parâmetros da busca {@link QueryObject}
     * @return Resultado da busca em um objeto SearchResult {@link SearchResult}
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "search", notes = "Faz uma pesquisa pela query informada através do objeto QueryObjet, os atributos são aq, q, start, pageSize, sortField, sortDir e searchFields. Além disso, possibilita filtar os atributos na saída através do parâmetro gumgaFields no header.")
    @RequestMapping(value = "lw", method = RequestMethod.GET)
    public SearchResult<T> pesquisa(HttpServletRequest request, QueryObject query) {
        SearchResult<T> pesquisa = service.pesquisa(query);
        String gumgaFields = request.getHeader("gumgaFields");
        if (gumgaFields != null) {
            String fields[] = gumgaFields.split(",");
            List<Map<String, Object>> toReturn = new ArrayList<>();
            for (Object obj : pesquisa.getValues()) {
                Map<String, Object> row = ReflectionUtils.objectFieldsToMap(fields, obj);
                toReturn.add(row);
            }
            return new SearchResult(query, pesquisa.getCount(), toReturn);
        }
        return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
    }

    /**
     * Faz uma pesquisa pela query informada através do objeto QueryObjet, cujos atributos são aq, q, start, pageSize, sortField, sortDir e searchFields
     * @param query Objeto QueryObject contendo os parâmetros da busca {@link QueryObject}
     * @return Resultado da busca em um objeto SearchResult {@link SearchResult}
     */
        @GumgaSwagger
    @Transactional
    @ApiOperation(value = "search", notes = "Faz uma pesquisa pela query informada através do objeto QueryObjet, os atributos são aq, q, start, pageSize, sortField, sortDir e searchFields.")
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<T> pesquisa(QueryObject query) {
        SearchResult<T> pesquisa = service.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
    }

    /**
     * Salva os parâmetros de uma busca avançada nos dados do usuário atual
     * @param qts Objeto QueryToSave contendo a busca a ser salva {@link QueryToSave}
     * @return String com o Status da transação se houve sucesso
     */
    @Transactional
    @ApiOperation(value = "saveQuery", notes = "Salva a consulta avançada.")
    @RequestMapping(value = "saq", method = RequestMethod.POST)
    public String save(@RequestBody @Valid QueryToSave qts, BindingResult result) {
        String key = "aq;" + qts.getPage() + ";" + qts.getName();
        String userLogin = GumgaThreadScope.login.get();
        GumgaUserData gud = guds.findByUserLoginAndKey(userLogin, key);
        if (gud == null) {
            gud = new GumgaUserData();
            gud.setUserLogin(userLogin);
            gud.setKey(key);
            gud.setDescription(qts.getName());
        }
        gud.setValue(qts.getData());
        guds.save(gud);
        return "OK";
    }

    /**
     * Carrega um objeto pelo id informado
     * @param id id da entidade a ser buscada
     * @return Objeto T correspondente ao id
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "load", notes = "Carrega entidade pelo id informado.")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public T load(@PathVariable ID id) {
        //String gumgaFields = request.getHeader("gumgaFields");
        T view = service.view(id);
        //if (gumgaFields != null) {
        //return ReflectionUtils.objectFieldsToMap(gumgaFields.split(","), view);
        //}
        return view;
    }

    /**
     * Carrega versões anteriores um objeto pelo id informado
     * @param id id da entidade a ser buscada
     * @return Lista contendo as versões antigas da entidade
     */
    @Transactional
    @ApiOperation(value = "listOldVersions", notes = "Mostra versões anteriores do objeto.")
    @RequestMapping(value = "listoldversions/{id}", method = RequestMethod.GET)
    public List<GumgaObjectAndRevision> listOldVersions(@PathVariable ID id) {
        return service.listOldVersions(id);
    }

    /**
     * Injeta uma entidade "Service" para acesso dos serviços do Framework
     * @param service Objeto GumgaServiceable T {@link GumgaServiceable}
     */
    public void setService(GumgaServiceable<T, ID> service) {
        this.service = service;
    }
    /**
     * Busca os dados associados a um usuário a partir da chave (Key) atribuída ao objeto
     * @param prefix Chave do objeto a ser buscado
     * @return Objeto resultado de uma busca {@link GumgaUserDataService}
     */
    @ApiOperation(value = "queryByKeyPrefix", notes = "Retorna os associados do usuário a uma chave.")
    @RequestMapping(value = "gumgauserdata/{prefix}", method = RequestMethod.GET)
    public SearchResult<GumgaUserData> queryByKeyPrefix(@PathVariable String prefix) {
        return ((GumgaUserDataService) guds).searchByKeyPrefix(prefix);
    }
    /**
     * Busca os tags a partir de um objeto de busca (QueryObject)
     * @param query Objeto QueryObject contendo os parâmetros de busca
     * @return Objeto SearchResult contendo o resultado da busca
     */
    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "tags")
    public SearchResult<GumgaTagDefinition> listAllTags(QueryObject query) {
        SearchResult<GumgaTagDefinition> pesquisa = gtds.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
    }
    /**
     * Busca uma lista de tags de um objeto específico a partir do id
     * @param objectId id do objeto a serem buscados os tags
     * @return Lista de GumgaTag {@link GumgaTag}
     */
    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "tags/{objectId}")
    public List<GumgaTag> listTagsOfEspecificObject(@PathVariable("objectId") Long objectId) {

        List<GumgaTag> tags = gts.findByObjectTypeAndObjectId(clazz().getCanonicalName(), objectId);
        for (GumgaTag tag : tags) {
            Hibernate.initialize(tag.getValues());
        }
        return tags;
    }

}
