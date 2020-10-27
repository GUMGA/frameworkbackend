package io.gumga.application;


import com.google.common.base.Strings;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.TenancyPublicMarking;
import io.gumga.core.gquery.Criteria;
import io.gumga.core.gquery.GQuery;
import io.gumga.domain.*;
import io.gumga.domain.domains.*;
import io.gumga.domain.logicaldelete.GumgaLD;
import io.gumga.domain.logicaldelete.GumgaLDModel;
import io.gumga.domain.logicaldelete.GumgaLDModelUUID;
import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.repository.GumgaMultitenancyUtil;
import io.gumga.domain.shared.GumgaSharedModel;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Parameter;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.or;


@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

    private EntityManager entityManager;
    private JpaEntityInformation<T, ?> entityInformation;

    public GumgaGenericRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    /**
     * Verificar se a entidade utiliza o Multitenancy da Gumga
     *
     * @return true se tiver e false caso não tiver anotada com
     * @{@link GumgaMultitenancy}
     */
    public boolean hasMultitenancy() {
        return entityInformation.getJavaType().isAnnotationPresent(GumgaMultitenancy.class);
    }

    public boolean hasBMO() {
        if (hasMultitenancy()) {
            GumgaMultitenancy annotation = entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class);
            return annotation.enableBMO() && (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType()));
        }
        return false;
    }

    public boolean hasLogicalDelete() {
        return GumgaLDModel.class.isAssignableFrom(entityInformation.getJavaType()) ||
                GumgaLDModelUUID.class.isAssignableFrom(entityInformation.getJavaType());
    }

    public SearchResult<T> aqoSearch(QueryObject query) {
        query.setAq(GumgaGenericRepositoryHelper.hql(query.getAqo()));
        return advancedSearch(query);
    }

    public SearchResult<T> findByGQuery(QueryObject queryObject) {
        if (queryObject.getgQuery() == null) {
            queryObject.setgQuery(new GQuery());
        }
        GQuery gQuery = queryObject.getgQuery();

        Long total = getCountResultPageGQuery(queryObject, gQuery);

        Query queryWithGQuery = createQueryGQuery(queryObject);

        return new SearchResult(queryObject, total, queryWithGQuery.getResultList());
    }

    public String getMultitenancyPattern() {
        GumgaMultitenancy tenacy = entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class);
        return GumgaMultitenancyUtil.getMultitenancyPattern(tenacy);
    }

    /**
     * Pesquisa sql por id utilizando o Multitenancy se a entidade estiver
     * anotada com @{@link GumgaMultitenancy}
     *
     * @param id
     * @return entidade tipada na interface @{@link GumgaCrudRepository}
     */
    @Override
    public T findOne(ID id) {
        if (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
            QueryObject qo = new QueryObject();
            if (GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
                qo.setAq("obj.id='" + id + "'");
            } else {
                qo.setAq("obj.id=" + id);
            }

            SearchResult<T> search = this.search(qo);
            if (search.getCount() == 1) {
                return search.getValues().get(0);
            }
            if (!GumgaThreadScope.ignoreCheckOwnership.get()) {
                throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
            }
        }

        Optional<T> fromDB = super.findById(id);

        if (!fromDB.isPresent()) {
            throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
        }
        T resource = fromDB.get();
        checkOwnership(resource);
        return resource;
    }

    @Override
    public SearchResult<T> search(QueryObject query) {
        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getOracleLikeMapWithAdjust())) {
            entityManager.createNativeQuery("alter session set nls_comp=linguistic").executeUpdate();
            entityManager.createNativeQuery("alter session set nls_sort=latin_ai").executeUpdate();
            entityManager.createNativeQuery("alter session set nls_date_format = 'YYYY-MM-DD'").executeUpdate();
            entityManager.createNativeQuery("alter session set nls_timestamp_format = 'YYYY-MM-DD HH24:MI:SS'").executeUpdate();
        }

        if (query.isGQuery()) {
            return findByGQuery(query);
        }

        if (query.isAQO()) {
            return aqoSearch(query);
        }

        if (query.isAdvanced()) {
            return advancedSearch(query);
        }
        Long count = 0l;
        if (query.isSearchCount()) {
            count = count(query);
        }

        List<T> data = query.isCountOnly() ? Collections.emptyList() : getOrdered(query);

        return new SearchResult<>(query, count, data);
    }

    @Override
    public Pesquisa<T> search() {
        return Pesquisa.createCriteria(session(), entityInformation.getJavaType());
    }

    @Override
    public SearchResult<T> search(String hql, Map<String, Object> params) {
        Query query = entityManager.createQuery(hql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> result = query.getResultList();
        int total = result.size();
        return new SearchResult<>(0, total, total, result);
    }

    @Override
    public SearchResult<T> search(String hql, Map<String, Object> params, int max, int first) {
        Query query = entityManager.createQuery(hql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        query.setMaxResults(max);
        query.setFirstResult(first);
        List<T> result = query.getResultList();
        int total = result.size();
        return new SearchResult<>(0, total, total, result);
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        List<GumgaObjectAndRevision> aRetornar = new ArrayList<>();
        AuditReader ar = AuditReaderFactory.get(entityManager);
        List<Number> revisoes = ar.getRevisions(entityInformation.getJavaType(), id);
        for (Number n : revisoes) {
            GumgaRevisionEntity gumgaRevisionEntity = entityManager.find(GumgaRevisionEntity.class, n.longValue());
            T object = ar.find(entityInformation.getJavaType(), id, n.longValue());
            aRetornar.add(new GumgaObjectAndRevision(gumgaRevisionEntity, object));
        }
        return aRetornar;
    }

    @Override
    public <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countQuery, String ordenationId, QueryObject whereQuery) {
        if (Strings.isNullOrEmpty(ordenationId)) {
            throw new IllegalArgumentException("Para realizar a search deve se informar um OrdenationId para complementar a ordenação");
        }

        String modelo = selectQueryWithoutWhere + " WHERE %s";
        String hqlConsulta;
        if (whereQuery.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo, whereQuery.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s, %s", whereQuery.getAq(), whereQuery.getSortField(), whereQuery.getSortDir(), ordenationId);
        }
        String hqlConta = countQuery + " WHERE " + whereQuery.getAq();
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(whereQuery.getPageSize());
        qConsulta.setFirstResult(whereQuery.getStart());
        List resultList = qConsulta.getResultList();

        return new SearchResult<>(whereQuery, total, resultList);
    }

    /**
     * Generic findOne
     *
     * @param clazz Classe a ser pesquisada
     * @param id    id a ser pesquisado
     * @return objeto encontrado ou null
     */
    @Override
    public Object genericFindOne(Class clazz, Object id) {
        Object result = entityManager.find(clazz, id);
        if (result == null) {
            throw new EntityNotFoundException("cannot find " + clazz.getName() + " with id: " + id);
        }
        checkOwnership(result);
        return result;
    }

    @Override
    public SearchResult<T> findAllWithTenancy() {
        QueryObject qo = new QueryObject();
        qo.setPageSize(Integer.MAX_VALUE);
        return search(qo);
    }

    @Override
    public T fetchOne(GQuery gQuery) {
        Query search = createQueryWithGQuery(gQuery);
        try {
            List<T> resultList = search.getResultList();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object fetchOneObject(GQuery gQuery) {
        Query search = createQueryWithGQuery(gQuery);
        try {
            org.hibernate.Query unwrap = search.unwrap(org.hibernate.Query.class);
            unwrap.setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP);
            List resultList = unwrap.list();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<T> findAll(GQuery gQuery) {
        Query search = createQueryWithGQuery(gQuery);
        search.setMaxResults(Integer.MAX_VALUE);

        List<T> resultList = search.getResultList();
        return resultList;
    }

    @Override
    public void deletePermanentGumgaLDModel(T entity) {
        super.delete(entity);
    }

    @Override
    public void deletePermanentGumgaLDModel(ID id) {
        super.delete(findOne(id));
    }

    @Override
    public SearchResult<Object> searchWithGQuery(QueryObject queryObject) {
        if (queryObject.getgQuery() == null) {
            queryObject.setgQuery(new GQuery());
        }
        GQuery gQuery = queryObject.getgQuery();

        Long total = getCountResultPageGQuery(queryObject, gQuery);

        Query query = createQueryGQuery(queryObject);
        org.hibernate.Query unwrap = query.unwrap(org.hibernate.Query.class);
        String selects = gQuery.getSelects();
        if (selects.length() > 0) {
            unwrap.setResultTransformer(org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP);
        } else {
            unwrap.setResultTransformer(org.hibernate.Criteria.ROOT_ENTITY);
        }

        return new SearchResult(queryObject, total, unwrap.list());
    }

    @Override
    public void deleteById(ID id) {
        super.deleteById(id);
    }

    /**
     * Remove a entidade tipada na interface @{@link GumgaCrudRepository} da
     * base dados
     *
     * @param id primary key da entidade a ser removida da base
     */
    @Override
    public void delete(ID id) {
        if (hasMultitenancy()) {
            checkOwnership(findOne(id));
        }
        super.deleteById(id);
    }

    /**
     * Remove a entidade tipada na interface @{@link GumgaCrudRepository} da
     * base dados
     *
     * @param entity entidade a ser removida
     */
    @Override
    public void delete(T entity) {
        if (hasMultitenancy()) {
            checkOwnership(entity);
        }
        if (hasLogicalDelete()) {
            ((GumgaLD) entity).setGumgaActive(Boolean.FALSE);
            return;
        }
        super.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        if (hasMultitenancy()) {
            for (T entity : entities) {
                delete(entity);
            }
        }
        super.deleteAll(entities);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        delete(entities);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        if (hasMultitenancy()) {
            //em razão do tenenacy ter que verificar um por um , é igual ao delete
            delete(entities);
        }
        super.deleteInBatch(entities);
    }

    @Override
    public void deleteAll() {
        if (hasMultitenancy()) {
            for (T entity : findAll()) {
                delete(entity);
            }
        } else {
            super.deleteAll();
        }
    }

    @Override
    public void deleteAllInBatch() {
        if (hasMultitenancy()) {
            for (T entity : findAll()) {
                delete(entity);
            }
        } else {
            super.deleteAllInBatch();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(findOne(id));
    }


    /**
     * Pesquisar a entidade tipada na interface @{@link GumgaCrudRepository} com
     * Multitenancy caso a entidade esteja anotada com
     *
     * @param id
     * @return resultado da pesquisa
     * @{@link GumgaMultitenancy}
     */
    @Override
    public T getOne(ID id) {
        return findOne(id);
    }

    @Override
    public boolean existsById(ID id) {
        return super.existsById(id);
    }

    /**
     * Verificar se objecto salvo no banco ja existe.
     *
     * @param id valor a ser pequisado na primary key da entidade
     * @return resultado da pesquisa
     */
    public boolean exists(ID id) {
        return super.existsById(id);
    }

    @Override
    public List<T> findAll() {
        return this.findAll(new GQuery());
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        if (ids == null || !ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        List<T> toReturn = new ArrayList<>();
        for (ID id : ids) {
            Object found = findOne(id);
            if (found != null) {
                toReturn.add((T) found);
            }
        }
        return toReturn;
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return findAll(ids);
    }

    @Override
    public List<T> findAll(Sort sort) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(pageable);
    }

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findOne(spec);
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec, pageable);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec, sort);
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findOne(example);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.exists(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(example, sort);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(example, pageable);
    }

    @Override
    public long count() {
        Long total = 0l;
        Query queryCountWithGQuery = createQueryCountWithGQuery(new GQuery());
        total = (Long) queryCountWithGQuery.getSingleResult();
        return total;
    }

    @Override
    public long count(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.count(spec);
    }

    @Override
    public <S extends T> S save(S entity) {
        return super.save(entity);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return super.saveAll(entities);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.saveAll(entities);
    }

    @Override
    public void flush() {
        super.flush();
    }

    private Query createQueryWithGQuery(GQuery gQuery) {
        Boolean useDistinct = gQuery.useDistinct();
        String selects = gQuery.getSelects();

        String query = (useDistinct ? "select distinct " : "select ") + (selects.length() > 0 ? selects : " obj ") + " FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return translateParameterGQuer(entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where)), gQuery);
    }

    private String createWhere(GQuery gQuery) {
        String gQueryWhere = gQuery.toString();

        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getMySqlLikeMap())) {
            gQueryWhere = convertTimestampToStrToDate(gQueryWhere)
                    .replaceAll("translate\\(", "")
                    .replaceAll(", 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç','AAAAAAAAEEEEIIOOOOOOUUUUCC'\\)", "");
        } else {
            if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getH2LikeMap())) {
            }
        }
        String whereLogicalDelete = "";

        if (hasLogicalDelete()) {
            String hash = Criteria.generateHash("obj.gumgaActive");
            gQuery.fieldValue.put(hash, Boolean.TRUE);
            whereLogicalDelete = String.format(" and obj.gumgaActive = :%s ", hash);
        }

        return getWhereMultiTenancyGQuery(gQuery).concat(whereLogicalDelete).concat(StringUtils.isEmpty(gQueryWhere) ? "" : " and ".concat(gQueryWhere));
    }

    private String removeFunctionToTimestamp(String gQueryWhere) {
        gQueryWhere = gQueryWhere.replaceAll("to_timestamp\\(", "")
                .replaceAll(",\\s*'yyyy/MM/dd HH24:mi:ss'\\)", "");
        return gQueryWhere;
    }

    private String convertTimestampToStrToDate(String gQueryWhere) {
        gQueryWhere = gQueryWhere.replaceAll("to_timestamp", "date_format")
                .replaceAll("yyyy/MM/dd HH24:mi:ss", "%Y/%m/%d %T");
        return gQueryWhere;
    }

    private Query translateParameterGQuer(Query query, GQuery gQuery) {

        gQuery
                .getParams()
                .forEach((key, value) -> {
                    Optional<Parameter<?>> parameterOptional = query
                            .getParameters()
                            .stream()
                            .filter(parameter -> parameter.getName().equals(key))
                            .findFirst();
                    if (parameterOptional.isPresent()) {
                        Parameter<?> parameter = parameterOptional.get();
                        if (parameter.getParameterType() != null) {
                            if (parameter.getParameterType().isEnum()) {
                                query.setParameter(key, Enum.valueOf((Class<Enum>) parameter.getParameterType(), value.toString()));
                            } else {
                                switch (parameter.getParameterType().getSimpleName()) {
                                    case "Long":
                                        query.setParameter(key, Long.valueOf(value.toString()));
                                        break;
                                    case "BigDecimal":
                                        query.setParameter(key, new BigDecimal(value.toString()));
                                        break;
                                    case "GumgaBarCode":
                                        query.setParameter(key, new GumgaBarCode(value.toString()));
                                        break;
                                    case "GumgaBoolean":
                                        query.setParameter(key, new GumgaBoolean(Boolean.valueOf(value.toString())));
                                        break;
                                    case "GumgaCEP":
                                        query.setParameter(key, new GumgaCEP(value.toString()));
                                        break;
                                    case "GumgaCNPJ":
                                        query.setParameter(key, new GumgaCNPJ(value.toString()));
                                        break;
                                    case "GumgaCPF":
                                        query.setParameter(key, new GumgaCPF(value.toString()));
                                        break;
                                    case "GumgaEMail":
                                        query.setParameter(key, new GumgaEMail(value.toString()));
                                        break;
                                    case "GumgaMoney":
                                        query.setParameter(key, new GumgaMoney(new BigDecimal(value.toString())));
                                        break;
                                    case "GumgaMultiLineString":
                                        query.setParameter(key, new GumgaMultiLineString(value.toString()));
                                        break;
                                    case "GumgaOi":
                                        query.setParameter(key, new GumgaOi(value.toString()));
                                        break;
                                    case "GumgaPhoneNumber":
                                        query.setParameter(key, new GumgaPhoneNumber(value.toString()));
                                        break;
                                    case "GumgaURL":
                                        query.setParameter(key, new GumgaURL(value.toString()));
                                        break;
                                    default:
                                        query.setParameter(key, value);
                                }
                            }
                        } else {
                            query.setParameter(key, value);
                        }
                    }
                });

        return query;
    }

    private String getWhereMultiTenancyGQuery(GQuery gQuery) {
        String tenant = " where ";


        if (hasMultitenancy() && GumgaThreadScope.organizationCode.get() != null && (GumgaThreadScope.ignoreCheckOwnership.get() == null || !GumgaThreadScope.ignoreCheckOwnership.get())) {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class));
            String objOi = Criteria.generateHash("obj.oi");
            gQuery.fieldValue.put(objOi, new GumgaOi(oiPattern + "%"));
            String oi = String.format("obj.oi is null or obj.oi like :%s", objOi);
            if (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
                String instanceOi = GumgaThreadScope.instanceOi.get() + GumgaSharedModel.GLOBAL;

                String objGumgaOrganizations = Criteria.generateHash("obj.gumgaOrganizations");
                gQuery.fieldValue.put(objGumgaOrganizations, "%," + oiPattern + ",%");

                String objGumgaOrganizationsInstance = Criteria.generateHash("obj.gumgaOrganizationsInstance");
                gQuery.fieldValue.put(objGumgaOrganizationsInstance, "%," + instanceOi + ",%");

                String objGumgaUsers = Criteria.generateHash("obj.gumgaUsers");
                gQuery.fieldValue.put(objGumgaUsers, "%," + GumgaThreadScope.login.get() + ",%");

                String objGumgaOrganizationsBase = "";
                String objGumgaOrganizationsMatrix = "";

                if (hasBMO()) {
                    String[] bmo = GumgaThreadScope.organizationCode.get().split("\\.");
                    if (bmo.length > 0) {
                        String hashBase = Criteria.generateHash("obj.gumgaOrganizationsBase");
                        gQuery.fieldValue.put(hashBase, "%," + String.format(GumgaSharedModel.SHARED_BASE, bmo[0]) + ",%");
                        objGumgaOrganizationsBase = String.format(" or obj.gumgaOrganizations like :%s ", hashBase);
                        if (bmo.length == 3) {
                            String hashMatrix = Criteria.generateHash("obj.gumgaOrganizationsMatrix");
                            gQuery.fieldValue.put(hashMatrix, "%," + String.format(GumgaSharedModel.SHARED_MATRIX, bmo[0], bmo[1]) + ",%");
                            objGumgaOrganizationsMatrix = String.format(" or obj.gumgaOrganizations like :%s ", hashMatrix);
                        }
                    }
                }

                tenant = tenant.concat("("
                        .concat(oi))
                        .concat(
                                String.format(" or obj.gumgaOrganizations like :%s or obj.gumgaOrganizations like :%s or obj.gumgaUsers like :%s %s %s)",
                                        objGumgaOrganizations,
                                        objGumgaOrganizationsInstance,
                                        objGumgaUsers,
                                        objGumgaOrganizationsBase,
                                        objGumgaOrganizationsMatrix));
            } else {
                tenant = tenant.concat("(").concat(oi).concat(")");
            }
        } else {
            tenant = tenant.concat(" 1=1");
        }
        return tenant;
    }

    private List<T> getOrdered(QueryObject query) {
        Pesquisa<T> pesquisa = getPesquisa(query);
        String sortField = query.getSortField();
        String sortType = query.getSortDir();

        if (!sortField.isEmpty()) {
            createAliasIfNecessary(pesquisa, sortField);
            getOrderField(pesquisa, sortField, sortType);

        } else {
            pesquisa.addOrder(asc("id")); //GUMGA-478
        }


        return pesquisa.setFirstResult(query.getStart()).setMaxResults(query.getPageSize()).list();
    }

    /**
     * Faz uma pesquisa no banco baseado na entidade que está tipada na
     * interface {@link GumgaCrudRepository}
     *
     * @param query
     * @return resultado da pesquisa
     */
    private SearchResult<T> advancedSearch(QueryObject query) {

        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getMySqlLikeMap())
                || GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getH2LikeMap())) {
            query.setAq(query.getAq().replaceAll("to_timestamp\\(", "").replaceAll(",'yyyy/MM/dd HH24:mi:ss'\\)", ""));
        }
        String modelo = "from %s obj WHERE %s";
        if (hasMultitenancy() && (GumgaThreadScope.ignoreCheckOwnership.get() == null || !GumgaThreadScope.ignoreCheckOwnership.get())) {
            String ld = "";
            if (hasLogicalDelete()) {
                ld = " obj.gumgaActive=" + (!query.isInactiveSearch()) + " and ";
            }
            GumgaMultitenancy gumgaMultiTenancy = getDomainClass().getAnnotation(GumgaMultitenancy.class);
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(gumgaMultiTenancy);
            String sharedCriterion = " ";

            if (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
                String instanceOi = GumgaThreadScope.instanceOi.get() + GumgaSharedModel.GLOBAL;

                sharedCriterion = "or (obj.gumgaOrganizations like '%%," + oiPattern + ",%%' or "
                        + "obj.gumgaOrganizations like '%%," + instanceOi + ",%%' or "
                        + "obj.gumgaUsers like '%%," + GumgaThreadScope.login.get() + ",%%') ";

                if (hasBMO()) {
                    String[] bmo = GumgaThreadScope.organizationCode.get().split("\\.");
                    if (bmo.length > 0) {
                        sharedCriterion = sharedCriterion
                                .concat(String
                                        .format(" or obj.gumgaOrganizations like '%s' ",
                                                "%%," + String.format(GumgaSharedModel.SHARED_BASE, bmo[0]) + ",%%"));
                        if (bmo.length == 3) {
                            sharedCriterion = sharedCriterion
                                    .concat(String
                                            .format(" or obj.gumgaOrganizations like '%s' ",
                                                    "%%," + String.format(GumgaSharedModel.SHARED_MATRIX, bmo[0], bmo[1]) + ",%%"));
                        }
                    }
                }
            }

            if (gumgaMultiTenancy.allowPublics()) {
                if (gumgaMultiTenancy.publicMarking() == TenancyPublicMarking.NULL) {
                    modelo = "from %s obj WHERE (" + ld + "obj.oi is null OR obj.oi like '" + oiPattern + "%%' " + sharedCriterion + ")  AND (%s) ";
                } else {
                    modelo = "from %s obj WHERE (" + ld + "obj.oi = '" + gumgaMultiTenancy.publicMarking().getMark() + "' OR obj.oi like '" + oiPattern + "%%' " + sharedCriterion + ")  AND (%s) ";
                }
            } else {
                modelo = "from %s obj WHERE (" + ld + "obj.oi like '" + oiPattern + "%%' " + sharedCriterion + ")  AND (%s) ";
            }
        }

        String hqlConsulta;
        if (query.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo + " ORDER BY obj.id ", entityInformation.getEntityName(), query.getAq());
        } else {
            String orderField = getOrderField(query.getSortField(), query.getSortDir());
            hqlConsulta = String.format(modelo + " ORDER BY %s", entityInformation.getEntityName(), query.getAq(), orderField);
        }

        Long total = 0l;
        if (query.isSearchCount()) {
            String hqlConta = String.format("SELECT count(obj) " + modelo, entityInformation.getEntityName(), query.getAq());
            Query qConta = entityManager.createQuery(hqlConta);
            total = (Long) qConta.getSingleResult();
        }


        Query qConsulta = entityManager.createQuery(hqlConsulta);
        qConsulta.setMaxResults(query.getPageSize());
        qConsulta.setFirstResult(query.getStart());
        List resultList = query.isCountOnly() ? Collections.emptyList() : qConsulta.getResultList();
        return new SearchResult<>(query, total, resultList);
    }

    private void getOrderField(Pesquisa<T> pesquisa, String sortField, String sorDir) {
        String orderColumns = sortField;
        String orderType = sorDir;

        String[] columns = getSplit(orderColumns);

        String[] types = getSplit(orderType);

        Boolean existsID = Boolean.FALSE;
        for (int i = 0; i < columns.length; i++) {
            pesquisa.addOrder(i < types.length && !types[i].trim().isEmpty() && types[i].trim().equalsIgnoreCase("desc") ? desc(columns[i]) : asc(columns[i]));
            if (columns[i].trim().equalsIgnoreCase("id")) {
                existsID = Boolean.TRUE;
            }
        }

        if (!existsID) {
            pesquisa.addOrder(asc("id"));
        }
    }

    private String getOrderField(String sortField, String sorDir) {
        if (sortField != null && sorDir != null && !sortField.trim().isEmpty()) {
            String orderColumns = sortField;
            String orderType = sorDir;

            String[] columns = getSplit(orderColumns);

            String[] types = getSplit(orderType);

            String ordem = "";
            Boolean existsID = Boolean.FALSE;
            for (int i = 0; i < columns.length; i++) {
                ordem = ordem.concat(columns[i]).concat(i < types.length ? " ".concat(types[i].trim().isEmpty() ? "asc" : types[i]) : " asc").concat(",");
                if (columns[i].trim().equalsIgnoreCase("id") || columns[i].trim().equalsIgnoreCase("obj.id")) {
                    existsID = Boolean.TRUE;
                }
            }

            if (!existsID) {
                ordem = ordem.concat("obj.id asc,");
            }

            return ordem.substring(0, ordem.length() - 1);
        }

        return "obj.id asc";
    }

    private String[] getSplit(String orderType) {
        String[] types = null;
        if (orderType.indexOf(",") > 0) {
            types = orderType.split(",");
        } else {
            types = new String[]{orderType};
        }
        return types;
    }

    private void createAliasIfNecessary(Pesquisa<T> pesquisa, String field) {
        String[] chain = field.split("\\.");

        if (chain.length <= 1) {
            return;
        }
        if (pesquisa.getAliases().contains(chain[0])) {
            return;
        }

        pesquisa.createAlias(chain[0], chain[0]);
        pesquisa.addAlias(chain[0]);
    }

    private Pesquisa<T> getPesquisa(QueryObject query) {
        if (query.getQ() == null && !query.isAdvanced()) {
            throw new IllegalArgumentException("Para realizar a pesquisa simples, q não pode ser nulo.");
        }

        if (query.getSearchFields() != null && query.getSearchFields().length == 0) {
            throw new IllegalArgumentException("Para realizar a search deve se informar pelo menos um campo a ser pesquisado.");
        }

        Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(entityInformation.getJavaType());
        Pesquisa<T> pesquisa = search().add(or(fieldsCriterions));

        if (hasMultitenancy() && GumgaThreadScope.organizationCode.get() != null) {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class));
            Criterion multitenancyCriterion;
            Criterion sharedCriterion;
            GumgaMultitenancy gumgaMultitenancy = getDomainClass().getAnnotation(GumgaMultitenancy.class);
            if (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
                String instanceOi = GumgaThreadScope.instanceOi.get() + GumgaSharedModel.GLOBAL;
                sharedCriterion = or(
                        like("gumgaOrganizations", "," + oiPattern + ",", MatchMode.ANYWHERE),
                        like("gumgaOrganizations", "," + instanceOi + ",", MatchMode.ANYWHERE),
                        like("gumgaUsers", "," + GumgaThreadScope.login.get() + ",", MatchMode.ANYWHERE)
                );

                if (hasBMO()) {
                    String[] bmo = GumgaThreadScope.organizationCode.get().split("\\.");
                    if (bmo.length > 0) {
                        sharedCriterion = or(
                                like("gumgaOrganizations", "," + oiPattern + ",", MatchMode.ANYWHERE),
                                like("gumgaOrganizations", "," + instanceOi + ",", MatchMode.ANYWHERE),
                                like("gumgaOrganizations", "," + String.format(GumgaSharedModel.SHARED_BASE, bmo[0]) + ",", MatchMode.ANYWHERE),
                                like("gumgaUsers", "," + GumgaThreadScope.login.get() + ",", MatchMode.ANYWHERE)
                        );
                        if (bmo.length == 3) {
                            sharedCriterion = or(
                                    like("gumgaOrganizations", "," + oiPattern + ",", MatchMode.ANYWHERE),
                                    like("gumgaOrganizations", "," + instanceOi + ",", MatchMode.ANYWHERE),
                                    like("gumgaOrganizations", "," + String.format(GumgaSharedModel.SHARED_BASE, bmo[0]) + ",", MatchMode.ANYWHERE),
                                    like("gumgaOrganizations", "," + String.format(GumgaSharedModel.SHARED_MATRIX, bmo[0], bmo[1]) + ",", MatchMode.ANYWHERE),
                                    like("gumgaUsers", "," + GumgaThreadScope.login.get() + ",", MatchMode.ANYWHERE)
                            );
                        }
                    }
                }

                if (gumgaMultitenancy.allowPublics()) {
                    if (gumgaMultitenancy.publicMarking() == TenancyPublicMarking.NULL) {
                        multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START), Restrictions.isNull("oi"), sharedCriterion);
                    } else {
                        multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START), Restrictions.eq("oi", gumgaMultitenancy.publicMarking().getMark()), sharedCriterion);
                    }
                } else {
                    multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START), sharedCriterion);
                }
            } else if (gumgaMultitenancy.allowPublics()) {
                if (gumgaMultitenancy.publicMarking() == TenancyPublicMarking.NULL) {
                    multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START), Restrictions.isNull("oi"));
                } else {
                    multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START), Restrictions.eq("oi", gumgaMultitenancy.publicMarking().getMark()));
                }
            } else {
                multitenancyCriterion = or(like("oi", oiPattern, MatchMode.START));
            }

            pesquisa.add(multitenancyCriterion);

        }

        if (hasLogicalDelete()) {
            pesquisa.add(Restrictions.eq("gumgaActive", !query.isInactiveSearch()));
        }

        if (query.getSearchFields() != null) {
            for (String field : query.getSearchFields()) {
                createAliasIfNecessary(pesquisa, field);
            }
        }

        return pesquisa;
    }

    private Long getCountResultPageGQuery(QueryObject queryObject, GQuery gQuery) {
        Long total = 0l;
        if (queryObject.isSearchCount()) {
            Query queryCountWithGQuery = createQueryCountWithGQuery(gQuery);
            total = (Long) queryCountWithGQuery.getSingleResult();
        }
        return total;
    }

    private Query createQueryCountWithGQuery(GQuery gQuery) {
        Boolean useDistinct = gQuery.useDistinct();
        String query = (useDistinct ? "select count(distinct obj) " : "select count(obj)") + " FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return translateParameterGQuer(entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where)), gQuery);
    }

    private Query createQueryGQuery(QueryObject queryObject) {
        Query queryWithGQuery = createQueryGQueryWithQueryObject(queryObject);

        queryWithGQuery.setMaxResults(queryObject.getPageSize());
        queryWithGQuery.setFirstResult(queryObject.getStart());
        return queryWithGQuery;
    }

    private Query createQueryGQueryWithQueryObject(QueryObject queryObject) {
        if (queryObject.getgQuery() == null) {
            queryObject.setgQuery(new GQuery());
        }
        GQuery gQuery = queryObject.getgQuery();
        String selects = gQuery.getSelects();
        String sort = getOrderField(queryObject.getSortField(), queryObject.getSortDir());
        Boolean useDistinct = gQuery.useDistinct();
        String query = (useDistinct ? "select distinct " : "select ") + (selects.length() > 0 ? selects : " obj ") + " FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return translateParameterGQuer(entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where).concat(" order by ").concat(sort)), gQuery);
    }

    private Long count(QueryObject query) {
        Object uniqueResult = getPesquisa(query).setProjection(rowCount()).uniqueResult();
        return uniqueResult == null ? 0L : ((Number) uniqueResult).longValue();
    }

    private void checkOwnership(Object o) throws EntityNotFoundException {
        if (GumgaThreadScope.ignoreCheckOwnership.get() != null && GumgaThreadScope.ignoreCheckOwnership.get()) {
            return;
        }
        if (!o.getClass().isAnnotationPresent(GumgaMultitenancy.class)) {
            return;
        }
        if (GumgaThreadScope.organizationCode.get() == null) {
            return;
        }

        if (o instanceof GumgaModel) {
            gumgaModel((GumgaModel) o);
        } else {
            if (o instanceof GumgaModelUUID) {
                gumgaModelUUID((GumgaModelUUID) o);
            } else {
                gumgaModelSharedUUID((GumgaSharedModelUUID) o);
            }
        }
    }

    private void gumgaModel(GumgaModel object) {

        if (hasMultitenancy()
                && (object.getOi() != null)
                && (GumgaThreadScope.organizationCode.get() == null || !object.getOi().getValue().startsWith(getMultitenancyPattern()))) {
            throw new EntityNotFoundException("cannot find object of " + entityInformation.getJavaType() + " with id: " + object.getId() + " in your organization: " + GumgaThreadScope.organizationCode.get());
        }
    }

    private void gumgaModelSharedUUID(GumgaSharedModelUUID object) {

        if (hasMultitenancy()
                && (object.getOi() != null)
                && (GumgaThreadScope.organizationCode.get() == null || !object.getOi().getValue().startsWith(getMultitenancyPattern()))) {
            throw new EntityNotFoundException("cannot find object of " + entityInformation.getJavaType() + " with id: " + object.getId() + " in your organization: " + GumgaThreadScope.organizationCode.get());
        }
    }

    private void gumgaModelUUID(GumgaModelUUID object) {

        if (hasMultitenancy()
                && (object.getOi() != null)
                && (GumgaThreadScope.organizationCode.get() == null || !object.getOi().getValue().startsWith(getMultitenancyPattern()))) {
            throw new EntityNotFoundException("cannot find object of " + entityInformation.getJavaType() + " with id: " + object.getId() + " in your organization: " + GumgaThreadScope.organizationCode.get());
        }
    }

    private String noMultiTenancyMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String msg;
        if (stackTrace.length >= 4) {
            msg = "The method '" + stackTrace[2].getMethodName() + "' in line " + stackTrace[2].getLineNumber() + " of class '" + stackTrace[2].getClassName()
                    + "' called from method '" + stackTrace[3].getMethodName() + "' of class '" + stackTrace[3].getClassName()
                    + "' has no implementation for MultiTenancy yet. Ask Gumga.";
        } else {
            msg = "No implementation for MultiTenancy yet. Ask Gumga.";
        }
        return msg;
    }

    private Session session() {
        return entityManager.unwrap(Session.class);
    }
}


