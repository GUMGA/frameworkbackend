package io.gumga.presentation.gateway;

import io.gumga.application.GumgaService;
import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.utils.ReflectionUtils;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.presentation.GumgaTranslator;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public abstract class GumgaReadOnlyGateway<A extends GumgaIdable<ID>, DTO, ID extends Serializable> implements GumgaReadableServiceable<DTO, ID> {
	
	@Autowired
	private GumgaService<A, ID> delegate;
	
	@Autowired
	private GumgaTranslator<A, DTO, ID> translator;
	
	@Override
	public SearchResult<DTO> pesquisa(QueryObject query) {
		SearchResult<A> pesquisa = delegate.pesquisa(query);
		return new SearchResult<>(query, pesquisa.getCount(), translator.from((List<A>) pesquisa.getValues()));
	}

	@Override
	public DTO view(ID id) {
		return translator.from(delegate.view(id));
	}

	//	@Override
//	public DTO view(Long id) {
//		return translator.from(delegate.view(id));
//	}
//
	@SuppressWarnings("unchecked")
	public Class<DTO> clazz() {
		return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
	}

}
