package io.gumga.domain.repository;

import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Cria um repositório para crude não tipado
 *
 * @author wlademir
 * @param <T> O tipo do objeto
 * @param <ID> O tipo do ID do objeto
 */
@NoRepositoryBean
public interface GumgaCrudAndQueryNotOnlyTypedRepository<T, ID extends Serializable> extends GumgaCrudRepository<T, ID> {

    /**
     * Query hql não tipada para consultas complexas. Retorna apenas um
     * registro. Não passa pelo multi tenancy, caso seja necessario deve ser
     * feito manualmente no hql implementado.
     *
     * @param <E> O tipo a ser retornado
     * @param hql A busca
     * @param type o Tipo de retorno
     * @param param Os parametros
     * @return Retorna um unico registro encontrado
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E> E getUniqueResult(String hql, Class<E> type, Map<String, Object> param);

    /**
     * Query hql não tipada para consultas complexas. Não passa pelo multi
     * tenancy, caso seja necessario deve ser feito manualmente no hql
     * implementado.
     *
     * @param hql A busca
     * @param param Os parametros
     * @return Uma lista contendo os registros enecontrados
     */
    public List getResultList(String hql, Map<String, Object> param);

    /**
     * Query hql não tipada para consultas complexas. Não passa pelo multi
     * tenancy, caso seja necessario deve ser feito manualmente no hql
     * implementado.
     *
     * @param hql A busca
     * @param param Os parametros
     * @param maxResult Quantidade maxima de registros retornados
     * @return Uma lista contendo os registros enecontrados
     *
     */
    public List getResultList(String hql, Map<String, Object> param, int maxResult);

    /**
     *
     * Query hql não tipada para consultas complexas. Não passa pelo multi
     * tenancy, caso seja necessario deve ser feito manualmente no hql
     * implementado.
     *
     * @param hql A busca
     * @param param Os parametros
     * @param firstresult Paginacao - indice inicial
     * @param maxResult Paginacal - total de registros da pagina
     * @return Uma lista contendo os registros enecontrados
     */
    public List getResultList(String hql, Map<String, Object> param, int firstresult, int maxResult);

    /**
     * JPAQuery instanciado para consultas complexas.
     *
     * @return O objeto JPAQuery
     */
    public JPAQuery getJPAQuerydsl();

}
