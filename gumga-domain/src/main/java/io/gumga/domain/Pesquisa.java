package io.gumga.domain;

import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.ResultTransformer;

import java.util.*;

/**
 * Representa um pesquisa com Criteria no framework
 */
public class Pesquisa<T> implements Criteria {

    private Set<String> aliases = new HashSet<>();

    private final Criteria criteria;
    private final Class<T> clazz;

    /**
     * Instancia um objeto do tipo Pesquisa para determinada classe e critério
     * @param clazz Classe
     * @param criteria Critério
     */
    public Pesquisa(Class<T> clazz, Criteria criteria) {
        this.clazz = clazz;
        this.criteria = criteria;
    }

    /**
     * Adiciona alias à consulta
     * @param alias alias
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Busca alias da consulta
     * @return Aliases da consulta
     */
    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    /**
     * Cria objeto de pesquisa com determinada sessão e classe
     * @param session Sessão
     * @param clazz Classe
     * @param <C> Tipo do retorno de pesquisa
     * @return Pesquisa
     */
    public static <C> Pesquisa<C> createCriteria(Session session, Class<C> clazz) {
        return new Pesquisa<>(clazz, session.createCriteria(clazz));
    }

    /**
     * Busca alias
     * @return Alias
     */
    @Override
    public String getAlias() {
        return criteria.getAlias();
    }

    /**
     * Configura projeção da pesquisa
     * @param projection Critério
     * @return Projeção
     */
    @Override
    public Criteria setProjection(Projection projection) {
        return criteria.setProjection(projection);
    }

    /**
     * Adiciona critério do hibernate à pesquisa
     * @param criterion Critério do hibernate
     * @return Pesquisa
     */
    @Override
    public Pesquisa<T> add(Criterion criterion) {
        criteria.add(criterion);
        return this;
    }

    /**
     * Adiciona ordenação para a pesquisa
     * @param order Ordenação
     * @return Critério de pesquisa
     */
    @Override
    public Criteria addOrder(Order order) {
        return criteria.addOrder(order);
    }

    /**
     * Configura tipo de associoção entre classes, se será utilizado JOIN ou SUBSELECT
     * @param associationPath Nome da associação
     * @param mode Tipo de associação
     * @return Critério do hibernate
     * @throws HibernateException Erro na configuração
     */
    @Override
    public Criteria setFetchMode(String associationPath, FetchMode mode) throws HibernateException {
        return criteria.setFetchMode(associationPath, mode);
    }

    /**
     * Configura tipo de bloqueio de alteração do registro quando ocorrerem interações simultâneas
     * @param lockMode Tipo de bloqueio (Lock)
     * @return Critério do hibernate
     */
    @Override
    public Criteria setLockMode(LockMode lockMode) {
        return criteria.setLockMode(lockMode);
    }

    /**
     * Configura tipo de bloqueio de alteração do registro para determinada consulta com determinado alias,  quando ocorrerem interações simultâneas
     * @param alias Alias
     * @param lockMode Tipo de bloqueio (Lock)
     * @return Critério do hibernate
     */
    @Override
    public Criteria setLockMode(String alias, LockMode lockMode) {
        return criteria.setLockMode(alias, lockMode);
    }

    /**
     * Cria alias em determinada associação
     * @param associationPath Associação
     * @param alias Alias
     * @return Critério de pesquisa
     * @throws HibernateException Erro na criação do alias
     */
    @Override
    public Criteria createAlias(String associationPath, String alias) throws HibernateException {
        return criteria.createAlias(associationPath, alias);
    }

    /**
     * Cria alias em determinada associação com determinada junção
     * @param associationPath Associação
     * @param alias Alias
     * @param joinType Tipo de junção
     * @return Critério de pesquisa
     * @throws HibernateException Erro na criação do alias
     */
    @Override
    public Criteria createAlias(String associationPath, String alias, JoinType joinType) throws HibernateException {
        return criteria.createAlias(associationPath, alias);
    }

    @Override
    @Deprecated
    public Criteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
        return criteria.createAlias(associationPath, alias, joinType);
    }

    /**
     * Cria alias em determinada associação com determinada junção, adicionando critério de pesquisa
     * @param associationPath Associação
     * @param alias Alias
     * @param joinType Tipo de junção
     * @param withClause Cláusula de pesquisa
     * @return Critério de pesquisa
     * @throws HibernateException Erro na criação do alias
     */
    @Override
    public Criteria createAlias(String associationPath, String alias, JoinType joinType, Criterion withClause) throws HibernateException {
        return criteria.createAlias(associationPath, alias, joinType);
    }

    @Override
    @Deprecated
    public Criteria createAlias(String associationPath, String alias, int joinType, Criterion withClause) throws HibernateException {
        return criteria.createAlias(associationPath, alias, joinType, withClause);
    }

    @Override
    public Criteria createCriteria(String associationPath) throws HibernateException {
        return criteria.createCriteria(associationPath);
    }

    @Override
    public Criteria createCriteria(String associationPath, JoinType joinType) throws HibernateException {
        return criteria.createCriteria(associationPath, joinType);
    }

    @Override
    @Deprecated
    public Criteria createCriteria(String associationPath, int joinType) throws HibernateException {
        return criteria.createCriteria(associationPath, joinType);
    }

    @Override
    public Criteria createCriteria(String associationPath, String alias) throws HibernateException {
        return criteria.createCriteria(associationPath, alias);
    }

    @Override
    public Criteria createCriteria(String associationPath, String alias, JoinType joinType) throws HibernateException {
        return criteria.createCriteria(associationPath, joinType);
    }

    @Override
    @Deprecated
    public Criteria createCriteria(String associationPath, String alias, int joinType) throws HibernateException {
        return criteria.createCriteria(associationPath, joinType);
    }

    @Override
    public Criteria createCriteria(String associationPath, String alias, JoinType joinType, Criterion withClause) throws HibernateException {
        return criteria.createCriteria(associationPath, alias, joinType, withClause);
    }

    @Override
    @Deprecated
    public Criteria createCriteria(String associationPath, String alias, int joinType, Criterion withClause) throws HibernateException {
        return criteria.createCriteria(associationPath, alias, joinType, withClause);
    }

    @Override
    public Criteria setResultTransformer(ResultTransformer resultTransformer) {
        return criteria.setResultTransformer(resultTransformer);
    }

    /**
     * Configura quantidade máxima de resultados
     * @param maxResults Quantidade máxima
     * @return Pesquisa
     */
    @Override
    public Pesquisa<T> setMaxResults(int maxResults) {
        criteria.setMaxResults(maxResults);
        return this;
    }

    /**
     * Configura índice de inicio da lista de resultados
     * @param firstResult Índice do primeiro resultado
     * @return Pesquisa
     */
    @Override
    public Pesquisa<T> setFirstResult(int firstResult) {
        criteria.setFirstResult(firstResult);
        return this;
    }

    /**
     * @return Modo somente leitura na inicialização
     */
    @Override
    public boolean isReadOnlyInitialized() {
        return criteria.isReadOnlyInitialized();
    }

    /**
     * @return Modo somente leitura
     */
    @Override
    public boolean isReadOnly() {
        return criteria.isReadOnly();
    }

    /**
     * Configura somente leitura
     * @param readOnly somente leitura
     * @return Critério
     */
    @Override
    public Criteria setReadOnly(boolean readOnly) {
        return criteria.setReadOnly(readOnly);
    }

    /**
     * Configura somente Tamanho da busca
     * @param fetchSize tamanho da busca
     * @return Critério
     */
    @Override
    public Criteria setFetchSize(int fetchSize) {
        return criteria.setFetchSize(fetchSize);
    }

    /**
     * Configura tempo limite da busca
     * @param timeout tempo limite da busca
     * @return Critério
     */
    @Override
    public Criteria setTimeout(int timeout) {
        return criteria.setTimeout(timeout);
    }

    /**
     * Configura se a busca será cacheada
     * @param cacheable cacheamento da busca
     * @return Critério
     */
    @Override
    public Criteria setCacheable(boolean cacheable) {
        return criteria.setCacheable(cacheable);
    }

    /**
     * Configura Região do cache
     * @param cacheRegion Região do cache
     * @return Critério
     */
    @Override
    public Criteria setCacheRegion(String cacheRegion) {
        return criteria.setCacheRegion(cacheRegion);
    }

    @Override
    public Criteria setComment(String comment) {
        return criteria.setComment(comment);
    }

    /**
     * Configura tipo de finalização da transação
     * @param flushMode Tipo de finalização da transação, exemplo: auto commit, manual...
     * @return Critério
     */
    @Override
    public Criteria setFlushMode(FlushMode flushMode) {
        return criteria.setFlushMode(flushMode);
    }

    /**
     * Configura tipo de cache
     * @param cacheMode Tipo de cache
     * @return Critério
     */
    @Override
    public Criteria setCacheMode(CacheMode cacheMode) {
        return criteria.setCacheMode(cacheMode);
    }

    /**
     * @return Lista de critérios
     * @throws HibernateException Exceção
     */
    @Override
    public List<T> list() throws HibernateException {
        List<T> newList = new LinkedList<>();
        for (Object obj : criteria.list()) {
            newList.add(clazz.cast(obj));
        }
        return newList;
    }

    /**
     * Método para busca de resultados anteriores e próximos, também conhecido como Infinite scroll
     * @See <a href="http://www.massapi.com/method/org/hibernate/Criteria.scroll.html">Exemplos</a>
     * @return Objeto {@link ScrollableResults}
     * @throws HibernateException Exceção na rolagem dos resultados
     */
    @Override
    public ScrollableResults scroll() throws HibernateException {
        return criteria.scroll();
    }

    /**
     * Método para busca de resultados anteriores e próximos, possibilitanto a configuração do tipo de rolagem dos dados (Infinite Scroll)
     * @See <a href="http://www.massapi.com/method/org/hibernate/Criteria.scroll.html">Exemplos</a>
     * @return Objeto {@link ScrollableResults}
     * @throws HibernateException Excessão na rolagem dos resultados
     */
    @Override
    public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
        return criteria.scroll(scrollMode);
    }

    /**
     * @return Resultado único
     * @throws HibernateException Exceção na busca
     */
    @Override
    public T uniqueResult() throws HibernateException {
        return clazz.cast(criteria.uniqueResult());
    }

    /**
     * Adiciona um dica (comentário) de consulta de banco dentro da consulta
     * @param hint Dica
     * @return Critério
     */
    @Override
    public Criteria addQueryHint(String hint) {
        return criteria.addQueryHint(hint);
    }

}
