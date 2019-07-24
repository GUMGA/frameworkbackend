package io.gumga.presentation.api;

import io.gumga.annotations.GumgaSwagger;
import io.gumga.domain.GumgaServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;
import io.gumga.domain.tag.GumgaTag;
import io.gumga.presentation.RestResponse;
import io.gumga.validation.exception.InvalidEntityException;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * API para acesso aos métodos de serviços que o Framework dispõe via requisições REST sem contemplar operações de exclusões
 * @param <T>
 * @param <ID>
 */
@RestController
public abstract class AbstractNoDeleteGumgaAPI<T, ID extends Serializable> extends
        AbstractReadOnlyGumgaAPI<T, ID> {

    protected GumgaWritableServiceable<T, ID> service;

    /**
     * Método construtor
     * @param service Injeção do Service utilizado para acesso aos serviços do Framework
     */
    public AbstractNoDeleteGumgaAPI(GumgaWritableServiceable<T, ID> service) {
        super(service);
        this.service = service;
    }

    /**
     * Salva o objeto T invocando o método de precedência
     * @param model Objeto T válido
     * @param result Objeto de validação
     * @return Resposta REST contendo a mensagem de entidade salva
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "save", notes = "Salva o objeto correspodente.")
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse<T> save(@RequestBody @Valid T model, BindingResult result) {
        beforeSave(model);
        T entity = saveOrCry(model, result);
        return new RestResponse<>(entity, getEntitySavedMessage(entity));
    }

    /**
     * Atualiza o objeto de acordo com o id correspondente
     * @param id id do objeto a ser atualizado
     * @param model Entidade T a ser atualizada
     * @param result Objeto de validação
     * @return Resposta REST contendo a mensagem de entidade atualizada
     */
    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "update", notes = "Atualiza o objeto pelo id correspondente.")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
    public RestResponse<T> update(@PathVariable("id") ID id,
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

    /**
     * Faz a injeção do objeto Service para acesso dos serviços do Framework
     * @param service Objeto GumgaServiceable T {@link GumgaServiceable}
     */
    @Override
    public void setService(GumgaServiceable<T, ID> service) {
        this.service = service;
        super.setService(service);
    }

    /**
     * Método executado antes de salvar o objeto T
     * @param model Objeto T a ser salvo
     */
    protected void beforeSave(T model) {

    }

    /**
     * Método executado antes de atualizar o objeto T
     * @param id Id da entidade T a ser atualizada
     * @param model Entidade T a ser salva
     */
    protected void beforeUpdate(ID id, T model) {

    }

    /**
     * Salva nos dados do usuário uma lista de Tags recebida por parâmetro
     * @param tags Objeto contendo uma lista de tags {@link TagsTo}
     */
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
