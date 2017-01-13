package io.gumga.presentation;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaServiceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class GumgaGateway<A extends GumgaIdable<ID>, ID extends Serializable, DTO> implements GumgaServiceable<DTO> {

    @Autowired
    protected GumgaService<A, ID> delegate;

    @Autowired
    protected GumgaTranslator<A, DTO> translator;

    @Override
    public SearchResult<DTO> pesquisa(QueryObject query) {
        SearchResult<A> pesquisa = delegate.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), translator.from((List<A>) pesquisa.getValues()));
    }

    @Override
    @Transactional(readOnly = true)
    public DTO view(Long id) {
        return translator.from(delegate.view(id));
    }

    @Override
    public void delete(DTO resource) {
        delegate.delete(translator.to(resource));
    }

    @Override
    public DTO save(DTO resource) {
        return translator.from(delegate.save(translator.to(resource)));
    }

    @SuppressWarnings("unchecked")
    public Class<DTO> clazz() {
        return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(Long id) {
        return Collections.EMPTY_LIST;
    }

}
