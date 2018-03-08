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

/**
 * Classe genérica para conversão de tipos de dados distintos
 * @param <A>
 * @param <ID>
 * @param <DTO>
 */
public class GumgaGatewayV2<A extends GumgaIdable<ID>, ID extends Serializable, DTO> implements GumgaServiceable<DTO, ID> {

    protected GumgaService<A, ID> delegate;
    protected GumgaTranslator<A, DTO, ID> translator;

    /**
     * Construtor com injeção dos módulos Service para acesso aos serviços do Framework e GumgaTranslator,
     * para conversão de tipos de dados distintos
     * {@link GumgaTranslator}
     * @param delegate Objeto Service A
     * @param translator
     */
    public GumgaGatewayV2(GumgaService<A, ID> delegate, GumgaTranslator<A, DTO, ID> translator) {
        this.delegate = delegate;
        this.translator = translator;
    }

    /**
     * Faz uma pesquisa de acordo com a entidade A, e retorna em uma entidade DTO da mesma
     * @param query Objeto QueryObject contendo os parâmetros da pesquisa
     * @return Resultado de pesquisa SearchResult da entidade DTO contendo o resultado da pesquisa
     */
    @Override
    public SearchResult<DTO> pesquisa(QueryObject query) {
        SearchResult<A> pesquisa = this.delegate.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), this.translator.from((List<A>) pesquisa.getValues()));
    }

    /**
     * Visualiza um objeto "traduzido" de acordo com o id informado
     * @param id id do objeto A
     * @return DTO do objeto A
     */
    @Override
    @Transactional(readOnly = true)
    public DTO view(ID id) {
        return this.translator.from(this.delegate.view(id));
    }

    /**
     * Deleta um objeto A de acordo com o recurso DTO recebido
     * @param resource Objeto DTO a ser deletado
     */
    @Override
    public void delete(DTO resource) {
        this.delegate.delete(this.translator.to(resource));
    }

    /**
     * Deleta uma coleção de objetos A de acordo com uma lista de recursos DTO recebidos
     * @param resource Lista contendo os objetos DTO a serem deletados
     */
    @Override
    public void delete(List<DTO> resource){
        this.delegate.delete(this.translator.to(resource));
    }

    @Override
    public void deletePermanentGumgaLDModel(DTO entity) {
        this.delegate.deletePermanentGumgaLDModel(this.translator.to(entity));
    }

    @Override
    public void deletePermanentGumgaLDModel(ID id) {
        this.delegate.deletePermanentGumgaLDModel(id);
    }

    /**
     * Salva um Objeto A de acordo com um recurso DTO recebido
     * @param resource Objeto DTO a ser salvo
     * @return Objeto A salvo
     */
    @Override
    public DTO save(DTO resource) {
        return this.translator.from(this.delegate.save(this.translator.to(resource)));
    }

    /**
     * Verifica qual a classe da instância DTO
     * @return Classe DTO
     */
    @SuppressWarnings("unchecked")
    public Class<DTO> clazz() {
        return (Class<DTO>) ReflectionUtils.inferGenericType(getClass());
    }

    /**
     * Lista as versões antigas da instância
     * @param id Id do objeto a ser buscado
     * @return Uma lista com todas as versões do objeto
     */
    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        return Collections.emptyList();
    }




}
