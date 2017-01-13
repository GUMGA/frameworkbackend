/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.application;

import com.mysema.query.jpa.impl.JPAQuery;
import io.gumga.domain.repository.GumgaCrudAndQueryNotOnlyTypedRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public class GumgaCrudAndQueryNotOnlyTypedRepositoryImpl<T, ID extends Serializable> extends GumgaGenericRepository<T, ID> implements GumgaCrudAndQueryNotOnlyTypedRepository<T, ID> {

    public GumgaCrudAndQueryNotOnlyTypedRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <E> E getUniqueResult(String hql, Class<E> type, Map<String, Object> param) {
        javax.persistence.Query query = entityManager.createQuery(hql);
        query.setMaxResults(1);
        addParam(query, param);
        return type.cast(query.getSingleResult());
    }

    @Override
    public List getResultList(String hql, Map<String, Object> param) {
        javax.persistence.Query query = entityManager.createQuery(hql);
        addParam(query, param);
        return query.getResultList();
    }

    @Override
    public List getResultList(String hql, Map<String, Object> param, int maxResult) {
        javax.persistence.Query query = entityManager.createQuery(hql);
        query.setMaxResults(maxResult);
        addParam(query, param);
        return query.getResultList();
    }

    @Override
    public List getResultList(String hql, Map<String, Object> param, int firstresult, int maxResult) {
        javax.persistence.Query query = entityManager.createQuery(hql);
        query.setMaxResults(maxResult);
        query.setFirstResult(firstresult);
        addParam(query, param);
        return query.getResultList();
    }

    @Override
    public JPAQuery getJPAQuerydsl() {
        JPAQuery jpa = new JPAQuery(entityManager);
        return jpa;
    }

    private void addParam(Query q, Map<String, Object> params) {
        if (params != null) {
            params.keySet().stream().forEach(key -> q.setParameter(key, params.get(key)));
        }
    }

}
