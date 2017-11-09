package io.gumga.presentation;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaServiceable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class GumgaGatewayV2<A extends GumgaIdable<ID>, ID extends Serializable, DTO> implements GumgaServiceable<DTO, ID> {

    protected GumgaService<A, ID> delegate;
    protected GumgaTranslator<A, DTO, ID> translator;

    public GumgaGatewayV2(GumgaService<A, ID> delegate, GumgaTranslator<A, DTO, ID> translator) {
        this.delegate = delegate;
        this.translator = translator;
    }

    @Override
    public SearchResult<DTO> pesquisa(QueryObject query) {
        SearchResult<A> pesquisa = this.delegate.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), this.translator.from((List<A>) pesquisa.getValues()));
    }

    @Override
    @Transactional(readOnly = true)
    public DTO view(ID id) {
        return this.translator.from(this.delegate.view(id));
    }

    @Override
    public void delete(DTO resource) {
        this.delegate.delete(this.translator.to(resource));
    }

    @Override
    public void delete(List<DTO> resource){
        this.delegate.delete(this.translator.to(resource));
    }

    @Override
    public DTO save(DTO resource) {
        return this.translator.from(this.delegate.save(this.translator.to(resource)));
    }

    @SuppressWarnings("unchecked")
    public Class<DTO> clazz() {
        return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        return Collections.emptyList();
    }
}
