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

    public Pesquisa(Class<T> clazz, Criteria criteria) {
        this.clazz = clazz;
        this.criteria = criteria;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public Set<String> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    public static <C> Pesquisa<C> createCriteria(Session session, Class<C> clazz) {
        return new Pesquisa<>(clazz, session.createCriteria(clazz));
    }

    @Override
    public String getAlias() {
        return criteria.getAlias();
    }

    @Override
    public Criteria setProjection(Projection projection) {
        return criteria.setProjection(projection);
    }

    @Override
    public Pesquisa<T> add(Criterion criterion) {
        criteria.add(criterion);
        return this;
    }

    @Override
    public Criteria addOrder(Order order) {
        return criteria.addOrder(order);
    }

    @Override
    public Criteria setFetchMode(String associationPath, FetchMode mode) throws HibernateException {
        return criteria.setFetchMode(associationPath, mode);
    }

    @Override
    public Criteria setLockMode(LockMode lockMode) {
        return criteria.setLockMode(lockMode);
    }

    @Override
    public Criteria setLockMode(String alias, LockMode lockMode) {
        return criteria.setLockMode(alias, lockMode);
    }

    @Override
    public Criteria createAlias(String associationPath, String alias) throws HibernateException {
        return criteria.createAlias(associationPath, alias);
    }

    @Override
    public Criteria createAlias(String associationPath, String alias, JoinType joinType) throws HibernateException {
        return criteria.createAlias(associationPath, alias);
    }

    @Override
    @Deprecated
    public Criteria createAlias(String associationPath, String alias, int joinType) throws HibernateException {
        return criteria.createAlias(associationPath, alias, joinType);
    }

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

    @Override
    public Pesquisa<T> setMaxResults(int maxResults) {
        criteria.setMaxResults(maxResults);
        return this;
    }

    @Override
    public Pesquisa<T> setFirstResult(int firstResult) {
        criteria.setFirstResult(firstResult);
        return this;
    }

    @Override
    public boolean isReadOnlyInitialized() {
        return criteria.isReadOnlyInitialized();
    }

    @Override
    public boolean isReadOnly() {
        return criteria.isReadOnly();
    }

    @Override
    public Criteria setReadOnly(boolean readOnly) {
        return criteria.setReadOnly(readOnly);
    }

    @Override
    public Criteria setFetchSize(int fetchSize) {
        return criteria.setFetchSize(fetchSize);
    }

    @Override
    public Criteria setTimeout(int timeout) {
        return criteria.setTimeout(timeout);
    }

    @Override
    public Criteria setCacheable(boolean cacheable) {
        return criteria.setCacheable(cacheable);
    }

    @Override
    public Criteria setCacheRegion(String cacheRegion) {
        return criteria.setCacheRegion(cacheRegion);
    }

    @Override
    public Criteria setComment(String comment) {
        return criteria.setComment(comment);
    }

    @Override
    public Criteria setFlushMode(FlushMode flushMode) {
        return criteria.setFlushMode(flushMode);
    }

    @Override
    public Criteria setCacheMode(CacheMode cacheMode) {
        return criteria.setCacheMode(cacheMode);
    }

    @Override
    public List<T> list() throws HibernateException {
        List<T> newList = new LinkedList<>();
        for (Object obj : criteria.list()) {
            newList.add(clazz.cast(obj));
        }
        return newList;
    }

    @Override
    public ScrollableResults scroll() throws HibernateException {
        return criteria.scroll();
    }

    @Override
    public ScrollableResults scroll(ScrollMode scrollMode) throws HibernateException {
        return criteria.scroll(scrollMode);
    }

    @Override
    public T uniqueResult() throws HibernateException {
        return clazz.cast(criteria.uniqueResult());
    }

    @Override
    public Criteria addQueryHint(String hint) {
        return criteria.addQueryHint(hint);
    }

}
