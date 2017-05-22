package io.gumga.presentation;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaServiceable;
import io.gumga.presentation.api.AbstractNoDeleteGumgaAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public abstract class AbstractGumgaAPI<T> extends AbstractNoDeleteGumgaAPI<T> {

    protected GumgaServiceable<T> service;

    public AbstractGumgaAPI(GumgaServiceable<T> service) {
        super(service);
        this.service = service;
    }

    @GumgaSwagger
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "delete", notes = "Deleta objeto com o id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public RestResponse<T> delete(@PathVariable Long id) {
        T entity = service.view(id);
        service.delete(entity);
        return new RestResponse<>(getEntityDeletedMessage(entity));
    }

    @GumgaSwagger
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "deletemulti", notes = "Deleta vários objeto com os ids correspondentes.")
    @RequestMapping(value = "multi/{id}", method = RequestMethod.DELETE)
    public RestResponse<T> delete(@PathVariable List<Long> ids) {
        List<T> entities = new ArrayList<>();
        for (Long id : ids) {
            entities.add(service.view(id));
        }
        service.delete(entities);
        RestResponse<T> restResponse = new RestResponse<>("Deleted " + ids);
        return restResponse;

    }

    public void setService(GumgaServiceable<T> service) {
        this.service = service;
        super.setService(service);
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "queryaction", notes = "Executa uma ação configurada para cada objeto resultante da pesquisa.")
    @RequestMapping(value = "queryaction", method = RequestMethod.POST)
    public Object queryAction(@RequestBody QueryObject query) {
        return selectElementsForAction(query);
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "multiselectionaction", notes = "Executa uma ação configurada para cada objeto da lista recebida.")
    @RequestMapping(value = "selectedaction", method = RequestMethod.POST)
    public Object multiSelectionAction(@RequestBody SelectionAndActionTO selectionAndActionTO) {
        return selectElementsForAction(selectionAndActionTO.action, selectionAndActionTO.ids);
    }

    public void doAction(String action, T obj) {
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.WARNING,action + "-----" + obj);
    }

    protected Object selectElementsForAction(String action, Long[] ids) {
        for (Long id : ids) {
            T view = service.view(id);
            doAction(action, view);
        }
        return new SelectionAndActionTO(action, ids);
    }

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
        public Long[] ids;

        public SelectionAndActionTO() {
        }

        public SelectionAndActionTO(String action, Long[] ids) {
            this.action = action;
            this.ids = ids;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Long[] getIds() {
            return ids;
        }

        public void setIds(Long[] ids) {
            this.ids = ids;
        }

    }

}
