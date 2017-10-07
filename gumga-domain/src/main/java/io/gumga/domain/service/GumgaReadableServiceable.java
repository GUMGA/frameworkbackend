package io.gumga.domain.service;

import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaObjectAndRevision;

import java.io.Serializable;
import java.util.List;

/**
 * Service com a operação de view, find, obter a classe e listas as versões
 * anteriores
 */
public interface GumgaReadableServiceable<T, ID extends Serializable> {

    /**
     * Pesquisa na entidade tipada na interface @{@link GumgaReadableServiceable}
     * @param queryObject filtro da busca da entidade
     * @return
     */
    public SearchResult<T> pesquisa(QueryObject queryObject);

    /**
     * Pesquisa a entidade tipada na interface @{@link GumgaReadableServiceable} pela primary key
     * @param id
     * @return
     */
    public T view(ID id);

    public Class<T> clazz();

    /**
     * Retornar as versões anteriores das entidades marcadas pelas auditoria
     * @param id
     * @return
     */
    public List<GumgaObjectAndRevision> listOldVersions(ID id);

}
