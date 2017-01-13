package io.gumga.domain.repository;

import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.domain.GumgaObjectAndRevision;
import io.gumga.domain.GumgaRepository;
import io.gumga.domain.Pesquisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Cria um repositório para CRUD TIPADO
 *
 * @author munif
 */
@NoRepositoryBean
public interface GumgaCrudRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, GumgaRepository<T, ID> {
    /**
     * Faz uma pesquisa no banco baseado na entidade que está tipada na interface @{@link GumgaCrudRepository}
     * @param query o dado a ser pesquisado. Link: {@link QueryObject}
     * @return resultados da pesquisa feita com os filtro do parametro query
     */
    SearchResult<T> search(QueryObject query);

    /**
     * Cria uma nova Pesquisa da entidade que está tipada na interface @{@link GumgaCrudRepository}. Link @{@link Pesquisa}
     * @return
     */
    Pesquisa<T> search();

    /**
     * Faz pesquisa baseado no hql que foi passado como parametro, nos parametros passados para filtrar e no numero maximo de retorno de dados e a posição inicial dos dados.
     *
     * @param hql
     * @param params a chave do Map é o parametro passado no hql e o valor do Map é o valor a ser pesquisado
     * @return os dados do hql
     */
    SearchResult<T> search(String hql, Map<String, Object> params);

    /**
     * Faz pesquisa baseado no hql que foi passado como parametro, nos parametros passados para filtrar e no numero maximo de retorno de dados e a posição inicial dos dados.
     *
     * @param hql select no formato de hql
     * @param params a chave do Map é o parametro passado no hql e o valor do Map é o valor a ser pesquisado
     * @param max limite de registro a serem retornados
     * @param first posição inicial dos dados
     * @return os dados do hql
     */
    SearchResult<T> search(String hql, Map<String, Object> params, int max, int first);

    /**
     * Retornar as versões anteriores das entidades marcadas pelas auditoria
     * @param id
     * @return
     */
    List<GumgaObjectAndRevision> listOldVersions(ID id);

    /**
     * Faz uma pesquisa no banco baseado na entidade que está tipada na interface @{@link GumgaCrudRepository}
     * @param selectQueryWithoutWhere
     * @param countObjt
     * @param ordenationId
     * @param whereQuery
     * @param <A>
     * @return
     */
    <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countObjt, String ordenationId, QueryObject whereQuery);

    /**
     *
     * @param clazz
     * @param id
     * @return
     */
    Object genericFindOne(Class clazz, Object id);

    /**
     * Pesquisa todas as entidades com o tenancy do ThreadScope
     * @return
     */
    SearchResult<T> findAllWithTenancy();

}
