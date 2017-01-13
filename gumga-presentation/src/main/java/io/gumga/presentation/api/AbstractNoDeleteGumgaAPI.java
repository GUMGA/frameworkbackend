package io.gumga.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import io.gumga.annotations.GumgaSwagger;
import io.gumga.domain.GumgaServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.presentation.RestResponse;
import io.gumga.validation.exception.InvalidEntityException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public abstract class AbstractNoDeleteGumgaAPI<T> extends
        AbstractReadOnlyGumgaAPI<T> {

    protected GumgaWritableServiceable<T> service;

    public AbstractNoDeleteGumgaAPI(GumgaWritableServiceable<T> service) {
        super(service);
        this.service = service;
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "save", notes = "Salva o objeto correspodente.")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<T> save(@RequestBody @Valid T model, BindingResult result) {
        beforeSave(model);
        T entity = saveOrCry(model, result);
        return new RestResponse<>(entity, getEntitySavedMessage(entity));
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "update", notes = "Atualiza o objeto pelo id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public RestResponse<T> update(@PathVariable("id") Long id,
            @Valid @RequestBody T model, BindingResult result) {
        beforeUpdate(id, model);
        T entity = saveOrCry(model, result);
        return new RestResponse<>(entity, getEntityUpdateMessage(entity));
    }

    private T saveOrCry(T model, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidEntityException(result);
        }

        return service.save(model);
    }

    @Override
    public void setService(GumgaServiceable<T> service) {
        this.service = service;
        super.setService(service);
    }

    protected void beforeSave(T model) {

    }

    protected void beforeUpdate(Long id, T model) {

    }

    @Transactional
    @RequestMapping(value = "tags", method = RequestMethod.POST)
    public void saveAll(@RequestBody TagsTo tags) {
        for (GumgaTag tag : tags.tags) {
            tag.setObjectType(clazz().getCanonicalName());
        }
        tags.tags.stream().forEach(t -> gts.save(t));
    }

}

class TagsTo {

    public List<GumgaTag> tags;

    public List<GumgaTag> getTags() {
        return tags;
    }

    public void setTags(List<GumgaTag> tags) {
        this.tags = tags;
    }

}
