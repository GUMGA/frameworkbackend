package io.gumga.presentation;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.domain.GumgaServiceable;
import io.gumga.presentation.api.AbstractNoDeleteGumgaAPI;
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

    public void setService(GumgaServiceable<T> service) {
        this.service = service;
        super.setService(service);
    }

}
