package io.gumga.application.service;

import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.service.GumgaReadableServiceable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service
@Scope("prototype")
public abstract class GumgaReadOnlyService<T extends GumgaIdable<?>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaReadableServiceable<T> {

    public GumgaReadOnlyService(GumgaCrudRepository<T, ID> repository) {
        super(repository);
    }

    /**
     * Processo executado antes do método Pesquisa da classe {@link GumgaReadableServiceable}
     * @param query
     */
    public void beforePesquisa(QueryObject query) {
    }

    /**
     * Processo executado apos do método Pesquisa da classe {@link GumgaReadableServiceable}
     * @param result
     */
    public void afterPesquisa(SearchResult<T> result) {
    }

    @Transactional(readOnly = true)
    public SearchResult<T> pesquisa(QueryObject query) {
        beforePesquisa(query);
        SearchResult<T> result = repository.search(query);
        afterPesquisa(result);

        return result;
    }

    /**
     * Processo executado antes do método view da classe {@link GumgaReadableServiceable}
     * @param id
     */
    public void beforeView(ID id) {
    }

    /**
     * Processo executado apos do método view da classe {@link GumgaReadableServiceable}
     * @param entity
     */
    public void afterView(T entity) {
    }

    /**
     * Pesquisa a entidade tipada na classe {@link GumgaReadableServiceable} pela primary key
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public T view(ID id) {
        beforeView(id);
        T entity = repository.findOne(id);
        loadGumgaCustomFields(entity);
        afterView(entity);
        return entity;
    }

    /**
     * Pesquisa a entidade passada no parametro pela primary key da entidade do parametro
     * @param clazz entidade a ser pesquisada
     * @param id primary key da entidade
     * @return
     */
    @Transactional(readOnly = true)
    public Object genercView(Class clazz, ID id) {
        Object entity = repository.genericFindOne(clazz, id);
        return entity;
    }

}
