package io.gumga.application.service;

import io.gumga.core.GumgaIdable;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.service.GumgaReadableServiceable;
import io.gumga.domain.service.GumgaWritableServiceable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * Classe abstrata que contém métodos para criação de serviços para manipulação de entidade (criação, alteração, busca exceto exclusão)
 * @param <T> Classe que contenha um identificador padrão, exemplo: ID do registro
 * @param <ID> Tipo do identificador contido na classe
 */
@Service
@Scope("prototype")
public abstract class GumgaNoDeleteService<T extends GumgaIdable<ID>, ID extends Serializable> extends AbstractGumgaService<T, ID> implements GumgaReadableServiceable<T, ID>, GumgaWritableServiceable<T, ID> {

	public GumgaNoDeleteService(GumgaCrudRepository<T, ID> repository) {
		super(repository);
	}

	/**
	 * Processo executado antes do método Pesquisa da classe {@link GumgaNoDeleteService}
	 * @param query Objeto de pesquisa
	 */
	public void beforePesquisa(QueryObject query) { }

	/**
	 * Processo executado apos do método Pesquisa da classe {@link GumgaNoDeleteService}
	 * @param result Resultado da pesquisa
	 */
	public void afterPesquisa(SearchResult<T> result) { }
	
	@Transactional(readOnly = true)
	public SearchResult<T> pesquisa(QueryObject query) {
		beforePesquisa(query);
		SearchResult<T> result = repository.search(query);
		afterPesquisa(result);
		
		return result;
	}

	/**
	 * Processo executado antes do método view da classe {@link GumgaNoDeleteService}
	 * @param id Id da Entidade
	 */
	public void beforeView(ID id) {}

	/**
	 * Processo executado apos do método view da classe {@link GumgaNoDeleteService}
	 * @param entity Id da Entidade
	 */
	public void afterView(T entity) {}

	/**
	 * Pesquisa a entidade tipada na classe {@link GumgaNoDeleteService} pela primary key
	 * @param id Id da Entidade
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
	 * Processo executado antes do método save e update da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	private void beforeSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			beforeSave(entity);
		else
			beforeUpdate(entity);
	}

	/**
	 * Processo executado apos do método save e update da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	private void afterSaveOrUpdate(T entity) {
		if (entity.getId() == null)
			afterSave(entity);
		else
			afterUpdate(entity);
	}

	/**
	 * Processo executado antes do método save da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	public void beforeSave(T entity) {}

	/**
	 * Processo executado antes do método update da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	public void beforeUpdate(T entity) {}

	/**
	 * Processo executado apos do método save da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	public void afterSave(T entity) {}

	/**
	 * Processo executado apos do método update da classe {@link GumgaNoDeleteService}
	 * @param entity Entidade
	 */
	public void afterUpdate(T entity) {}

	/**
	 * Salvar entidade na base de dados
	 * @param resource Entidade a ser salva
	 * @return
	 */
	@Transactional
	public T save(T resource) {
		beforeSaveOrUpdate(resource);
		T entity = repository.save(resource);
                gces.saveCustomFields(resource);
		afterSaveOrUpdate(entity);
		
		return entity;
	}

	/**
	 * Sincronizar os dados do {@link javax.persistence.EntityManager} com o banco de dados.
	 */
	public void forceFlush() {
		repository.flush();
	}
	
}
