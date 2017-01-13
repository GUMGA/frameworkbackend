package io.gumga.application;

import io.gumga.application.service.AbstractGumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaMultitenancy;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaServiceable;
import io.gumga.domain.repository.GumgaCrudRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Scope("prototype")
public abstract class GumgaService<T extends GumgaIdable<ID>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaServiceable<T> {

    public GumgaService(GumgaCrudRepository<T, ID> repository) {
        super(repository);
    }

    /**
     * Processo executado antes do Método pesquisa da classe @{@link GumgaService}
     * @param query
     */
    public void beforePesquisa(QueryObject query) {
    }

    /**
     * Processo executado apos do Método pesquisa da classe @{@link GumgaService}
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
     * Processo executado antes do Método view da classe @{@link GumgaService}
     * @param id
     */
    public void beforeView(ID id) {
    }

    /**
     * Processo executado apos o Método view da classe @{@link GumgaService}
     * @param entity
     */
    public void afterView(T entity) {
    }

    /**
     * Pesquisa a entidade na base de dados por primary key
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
     * Procura a entidade pela primary key
     * @param clazz entidade a ser procurada
     * @param id primary key da entidade
     * @return
     */
    @Transactional(readOnly = true)
    public Object genercView(Class clazz, ID id) {
        Object entity = repository.genericFindOne(clazz, id);
        return entity;
    }

    /**
     * Processo executado antes do Método delete da classe {@link GumgaServiceable}
     * @param entity
     */
    public void beforeDelete(T entity) {
    }

    /**
     * Processo executado apos do Método delete da classe {@link GumgaServiceable}
     */
    public void afterDelete() {
    }

    /**
     * Remove a entidade da base de dados
     * @param resource entidade a ser removida
     */
    @Transactional
    public void delete(T resource) {
        beforeDelete(resource);
        repository.delete(resource);
        if (gces != null) {
            gces.deleteCustomFields(resource);
        }
        afterDelete();
    }

    private void beforeSaveOrUpdate(T entity, boolean isNew) {
        if (isNew) {
            beforeSave(entity);
        } else {
            beforeUpdate(entity);
        }
    }

    private void afterSaveOrUpdate(T entity, boolean isNew) {
        if (isNew) {
            afterSave(entity);
        } else {
            afterUpdate(entity);
        }
    }

    /**
     * Processo executado antes do Método save da classe {@link GumgaServiceable}
     * @param entity
     */
    public void beforeSave(T entity) {
    }

    /**
     * Processo executado antes do Método update da classe {@link GumgaServiceable}
     * @param entity
     */
    public void beforeUpdate(T entity) {
    }

    /**
     * Processo executado apos do Método save da classe {@link GumgaServiceable}
     * @param entity
     */
    public void afterSave(T entity) {
    }

    /**
     * Processo executado apos do Método update da classe {@link GumgaServiceable}
     * @param entity
     */
    public void afterUpdate(T entity) {
    }

    /**
     * Salva a entidade na base de dados com Multitenancy se a entidade estiver anotada com {@link GumgaMultitenancy}
     * @param resource
     * @return
     */
    @Transactional
    public T save(T resource) {
        boolean isNew = (resource.getId() == null);

        beforeSaveOrUpdate(resource, isNew);
        T entity = repository.save(resource);
        if (gces != null) {
            gces.saveCustomFields(resource);
        }
        afterSaveOrUpdate(entity, isNew);

        return entity;
    }

    /**
     * Sincronizar os dados do EntityManager com o banco de dados
     */
    public void forceFlush() {
        repository.flush();
    }

    /**
     * Retornar as versões anteriores das entidades marcadas pelas auditoria
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        List<GumgaObjectAndRevision> oldVersions = repository.listOldVersions(id);
        return oldVersions;
    }

}
