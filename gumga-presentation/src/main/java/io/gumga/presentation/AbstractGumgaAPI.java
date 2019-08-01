package io.gumga.presentation;

import io.gumga.annotations.GumgaSwagger;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.gquery.GQuery;
import io.gumga.domain.GumgaServiceable;
import io.gumga.presentation.api.AbstractNoDeleteGumgaAPI;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * API para acesso aos métodos de serviços que o Framework dispõe via requisições REST
 * @param <T>
 * @param <ID>
 */
@RestController
public abstract class AbstractGumgaAPI<T, ID extends Serializable> extends AbstractNoDeleteGumgaAPI<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(AbstractGumgaAPI.class);

    protected GumgaServiceable<T, ID> service;

    public AbstractGumgaAPI(GumgaServiceable<T, ID> service) {
        super(service);
        this.service = service;
    }

    /**
     * Deleta objeto de acordo com o id recebido
     * {@code 200 OK}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">HTTP/1.1: Semantics and Content, section 6.3.1</a>
     * @param id Id do objeto a ser deletado
     * @return Objeto RestResponse contendo a mensagem de Entidade Deletada
     */
    @GumgaSwagger
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "delete", notes = "Deleta objeto com o id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RestResponse<T> delete(@PathVariable ID id) {
        T entity = service.view(id);
        service.delete(entity);
        return new RestResponse<>(getEntityDeletedMessage(entity));
    }

    /**
     * Deleta objeto marcado com remoção lógica permanentemente de acordo com o id recebido
     * {@code 200 OK}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">HTTP/1.1: Semantics and Content, section 6.3.1</a>
     * @param id Id do objeto a ser deletado
     * @return Objeto RestResponse contendo a mensagem de Entidade Deletada
     */
    @GumgaSwagger
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "deletePermanentGumgaLDModel", notes = "Deleta objeto de exclusão lógica permanentemente com o id correspondente.")
    @RequestMapping(value = "/{id}/permanent", method = RequestMethod.DELETE)
    public RestResponse<T> deletePermanentGumgaLDModel(@PathVariable ID id) {
        T entity = service.view(id);
        service.deletePermanentGumgaLDModel(id);
        return new RestResponse<>(getEntityDeletedMessage(entity));
    }


    /**
     * Deleta uma série de objetos de acordo com os id's recebidos
     * {@code 200 OK}.
     * @see <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">HTTP/1.1: Semantics and Content, section 6.3.1</a>
     * @param ids Lista de ids a serem deletados
     * @return
     */
    @GumgaSwagger
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "deletemulti", notes = "Deleta vários objeto com os ids correspondentes.")
    @RequestMapping(value = "multi/{id}", method = RequestMethod.DELETE)
    public RestResponse<T> delete(@PathVariable List<ID> ids) {
        List<T> entities = new ArrayList<>();
        for (ID id : ids) {
            entities.add(service.view(id));
        }
        service.delete(entities);
        RestResponse<T> restResponse = new RestResponse<>("Deleted " + ids);
        return restResponse;

    }

    /**
     * Injeta um módulo Service T para acesso aos serviços do Framework
     * @param service Objeto GumgaServiceable T {@link GumgaServiceable}
     */
    public void setService(GumgaServiceable<T, ID> service) {
        this.service = service;
        super.setService(service);
    }

    /**
     * Executa uma ação configurada para cada objeto resultante da pesquisa
     * @param query Objeto QueryObject a ser pesquisado
     * @return Objeto SearchResult com o resultado da pesquisa
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "queryaction", notes = "Executa uma ação configurada para cada objeto resultante da pesquisa.")
    @RequestMapping(value = "queryaction", method = RequestMethod.POST)
    public Object queryAction(@RequestBody QueryObject query) {
        return selectElementsForAction(query);
    }

    /**
     * Executa uma ação configurada para cada objeto da lista recebida
     * @param selectionAndActionTO Objeto SelectionAndActionTO
     * @return
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "multiselectionaction", notes = "Executa uma ação configurada para cada objeto da lista recebida.")
    @RequestMapping(value = "selectedaction", method = RequestMethod.POST)
    public Object multiSelectionAction(@RequestBody SelectionAndActionTO selectionAndActionTO) {
        return selectElementsForAction(selectionAndActionTO.action, (ID[]) selectionAndActionTO.ids);
    }

    /**
     * Executa uma ação
     * @param action String com a ação a ser realizada
     * @param obj Objeto T
     */
    public void doAction(String action, T obj) {
        log.info(action + "-----" + obj);
    }

    /**
     * Seleciona elementos para uma ação
     * @param action String contendo a ação a ser realizada
     * @param ids Array contendo os id's para seleção
     * @return objeto selectElementsForAction
     */
    protected Object selectElementsForAction(String action, ID[] ids) {
        for (ID id : ids) {
            T view = service.view(id);
            doAction(action, view);
        }
        return new SelectionAndActionTO(action, ids);
    }

    /**
     *Seleciona elementos para uma ação a partir de uma query
     * @param queryObject Objeto QueryObject com a busca a ser realizada
     * @return Objeto SearchResult contendo o resultado da busca
     */
    protected Object selectElementsForAction(QueryObject queryObject) {
        queryObject.setPageSize(Integer.MAX_VALUE);
        queryObject.setStart(0);
        SearchResult<T> pesquisa = service.pesquisa(queryObject);
        for (T c : pesquisa.getValues()) {
            doAction(queryObject.getAction(), c);
        }
        return new SearchResult<>(queryObject, pesquisa.getCount(), pesquisa.getValues());
    }

    private static class SelectionAndActionTO {

        public String action;
        public Object[] ids;

        public SelectionAndActionTO() {
        }

        public SelectionAndActionTO(String action, Object[] ids) {
            this.action = action;
            this.ids = ids;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Object[] getIds() {
            return ids;
        }

        public void setIds(Object[] ids) {
            this.ids = ids;
        }

    }

    /**
     * Rota de entrada para buscas via GQuery
     * {@link GQuery}
     * @param query Objeto QueryObject contendo a busca a ser realizada
     * @return Objeto SearchResult T contendo o resultado da busca
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "gquery", notes = "gquery")
    @RequestMapping(path = "/gquery", method = RequestMethod.POST)
    public SearchResult<T>  qquery(@RequestBody QueryObject query) {
        return service.pesquisa(query);
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "gqueryV2", notes = "gqueryV2")
    @RequestMapping(path = "/v2/gquery", method = RequestMethod.POST)
    public SearchResult<Object>  searchWithGQuery(@RequestBody QueryObject query) {
        return service.searchWithGQuery(query);
    }

}
