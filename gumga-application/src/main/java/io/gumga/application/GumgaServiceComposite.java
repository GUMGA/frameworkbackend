package io.gumga.application;

import io.gumga.application.service.AbstractGumgaService;
import io.gumga.core.GumgaIdable;

import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaModelUUIDCompositePK;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaServiceable;
import io.gumga.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.util.List;

public abstract class GumgaServiceComposite<T extends GumgaIdable<GumgaModelUUIDCompositePK>, ID extends Serializable> extends AbstractGumgaService<T, GumgaModelUUIDCompositePK> implements GumgaServiceable<T, ID> {

    public GumgaServiceComposite(GumgaCrudRepository<T, GumgaModelUUIDCompositePK> repository) {
        super(repository);
    }

    @Override
    public void delete(T resource) {
        this.repository.delete(resource);
    }

    @Override
    public void delete(List<T> resource) {
        resource.forEach(this::delete);
    }

    @Override
    public void deletePermanentGumgaLDModel(T entity) {
        this.repository.deletePermanentGumgaLDModel(entity);
    }

    @Override
    public void deletePermanentGumgaLDModel(ID id) {
        this.repository.deletePermanentGumgaLDModel(new GumgaModelUUIDCompositePK(id.toString()));
    }

    @Override
    public T save(T resource) {
        return this.repository.save(resource);
    }

    @Override
    public SearchResult<T> pesquisa(QueryObject queryObject) {
        return this.repository.search(queryObject);
    }

    @Override
    public SearchResult<Object> searchWithGQuery(QueryObject queryObject) {
        return this.repository.searchWithGQuery(queryObject);
    }

    @Override
    public T view(ID id) {
        return this.repository.findOne(new GumgaModelUUIDCompositePK(id.toString()));
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        return this.repository.listOldVersions(new GumgaModelUUIDCompositePK(id.toString()));
    }
}

