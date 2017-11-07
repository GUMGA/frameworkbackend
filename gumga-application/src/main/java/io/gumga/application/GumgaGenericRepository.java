package io.gumga.application;

import com.google.common.base.Strings;
import io.gumga.core.GumgaThreadScope;
import io.gumga.core.QueryObject;
import io.gumga.core.SearchResult;
import io.gumga.core.TenancyPublicMarking;
import io.gumga.core.gquery.ComparisonOperator;
import io.gumga.core.gquery.Criteria;
import io.gumga.core.gquery.GQuery;
import io.gumga.domain.*;
import io.gumga.domain.logicaldelete.GumgaLDModel;
import io.gumga.domain.repository.GumgaCrudRepository;
import io.gumga.domain.repository.GumgaMultitenancyUtil;
import io.gumga.domain.shared.GumgaSharedModel;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.internal.SQLQueryImpl;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.isNull;
import static org.hibernate.criterion.Restrictions.like;
import static org.hibernate.criterion.Restrictions.or;

@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

    protected final JpaEntityInformation<T, ID> entityInformation;
    protected final EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(GumgaGenericRepository.class);

    public GumgaGenericRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public SearchResult<T> search(QueryObject query) {
        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getOracleLikeMapWithAdjust())) {
            log.warn("ORACLE ADJUST");
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

        Long count = count(query);
        List<T> data = query.isCountOnly() ? Collections.emptyList() : getOrdered(query);

        return new SearchResult<>(query, count, data);
    }

    public SearchResult<T> aqoSearch(QueryObject query) {
        query.setAq(GumgaGenericRepositoryHelper.hql(query.getAqo()));
        return advancedSearch(query);
    }

    private List<T> getOrdered(QueryObject query) {
        Pesquisa<T> pesquisa = getPesquisa(query);
        String sortField = query.getSortField();
        String sortType = query.getSortDir();

        if (!sortField.isEmpty()) {
            createAliasIfNecessary(pesquisa, sortField);
            getOrderField(pesquisa, sortField, sortType);
//            pesquisa.addOrder();
//            pesquisa.addOrder("asc".equals(sortType) ? asc(sortField).ignoreCase() : desc(sortField).ignoreCase());
        } else {
            pesquisa.addOrder(asc("id")); //GUMGA-478
        }


        return pesquisa.setFirstResult(query.getStart()).setMaxResults(query.getPageSize()).list();
    }

    private Long count(QueryObject query) {
        Object uniqueResult = getPesquisa(query).setProjection(rowCount()).uniqueResult();
        return uniqueResult == null ? 0L : ((Number) uniqueResult).longValue();
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
                        //  like("1","1",MatchMode.EXACT)
                        like("gumgaOrganizations", "," + oiPattern + ",", MatchMode.ANYWHERE),
                        like("gumgaOrganizations", "," + instanceOi + ",", MatchMode.ANYWHERE),
                        like("gumgaUsers", "," + GumgaThreadScope.login.get() + ",", MatchMode.ANYWHERE)
                );

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
            if (hasLogicalDelete()) {
                pesquisa.add(Restrictions.eq("gumgaActive", !query.isInactiveSearch()));
            }

        }

        if (query.getSearchFields() != null) {
            for (String field : query.getSearchFields()) {
                createAliasIfNecessary(pesquisa, field);
            }
        }

        return pesquisa;
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

    public boolean hasLogicalDelete() {
        return GumgaLDModel.class.isAssignableFrom(entityInformation.getJavaType());
    }

    public String getMultitenancyPattern() {
        GumgaMultitenancy tenacy = entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class);
        return GumgaMultitenancyUtil.getMultitenancyPattern(tenacy);
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

    @Override
    public Pesquisa<T> search() {
        return Pesquisa.createCriteria(session(), entityInformation.getJavaType());
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
//            T result = fetchOne(new GQuery(new Criteria("obj.id", ComparisonOperator.EQUAL, id)));
//            if(result != null) {
//                return result;
//            } else {
//                if (!GumgaThreadScope.ignoreCheckOwnership.get()) {
//                    throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
//                }
//            }
            QueryObject qo = new QueryObject();
            qo.setAq("obj.id=" + id);
            SearchResult<T> search = this.search(qo);
            if (search.getCount() == 1) {
                return search.getValues().get(0);
            }
            if (!GumgaThreadScope.ignoreCheckOwnership.get()) {
                throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
            }
        }

        T resource = super.findOne(id);

        if (resource == null) {
            throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
        }
        checkOwnership(resource);
        return resource;
    }

    /**
     * Generic findOne
     *
     * @param clazz Classe a ser pesquisada
     * @param id id a ser pesquisado
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

    private Session session() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Faz uma pesquisa no banco baseado na entidade que está tipada na
     * interface {@link GumgaCrudRepository}
     *
     * @param query
     * @return resultado da pesquisa
     */
    private SearchResult<T> advancedSearch(QueryObject query) {
//        System.out.println("---RECEBIDA----->"+query.getAq());
//        if (query.getAq().startsWith("{")) {
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                Map readValue = mapper.readValue(query.getAq(), Map.class);
//                query.setAq(readValue.get("hql").toString());
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//        List<QueryObjectElement> qoeFromString = GumgaGenericRepositoryHelper.qoeFromString(query.getAqo());
//        String hqlFromQes = "";// GumgaGenericRepositoryHelper.hqlFromQoes(entityInformation,qoeFromString);
//
//        if (!QueryObject.EMPTY.equals(query.getAqo())) {
//            //query.setAq(hqlFromQes);
//        }
        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getMySqlLikeMap())
                || GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getH2LikeMap())) {
            query.setAq(query.getAq().replaceAll("to_timestamp\\(", "").replaceAll(",'yyyy/MM/dd HH24:mi:ss'\\)", ""));
        }
//        System.out.println("---CONVERTIDA----->"+query.getAq());
        String modelo = "from %s obj WHERE %s";
        if (hasMultitenancy()) {
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
        String hqlConta = String.format("SELECT count(obj) " + modelo, entityInformation.getEntityName(), query.getAq());
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(query.getPageSize());
        qConsulta.setFirstResult(query.getStart());
        List resultList = query.isCountOnly() ? Collections.emptyList() : qConsulta.getResultList();
        return new SearchResult<>(query, total, resultList);
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
    protected TypedQuery<Long> getCountQuery(Specification<T> spec) {
        return super.getCountQuery(spec);
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
        return super.getQuery(spec, sort);
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
        return super.getQuery(spec, pageable);
    }

    @Override
    protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        return super.readPage(query, pageable, spec);
    }

    @Override
    public void flush() {
        super.flush();
    }

    /**
     * salvar entidades utilizando o Multitenancy se a entidade estiver anotada
     * com @{@link GumgaMultitenancy}
     *
     * @param entities
     * @param <S>
     * @return lista das entidades salvas
     */
    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(entities);
    }

    /**
     * salvar entidade utilizando o Multitenancy se a entidade estiver com
     *
     * @{@link GumgaMultitenancy}
     *
     * @param entity
     * @param <S>
     * @return entidade salva
     */
    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity);
    }

    /**
     * salvar entidade utilizando o Multitenancy se a entidade estiver anotada
     * com @{@link GumgaMultitenancy}
     *
     * @param entity
     * @param <S>
     * @return entidade salva
     */
    @Override
    public <S extends T> S save(S entity) {
        return super.save(entity);
    }

    @Override
    public long count(Specification<T> spec) {
        return super.count(spec);
    }

    /**
     * Procurar a quantiadde de registro na base dados baseado na entidade
     * tipada na interface @{@link GumgaCrudRepository}
     *
     * @return quantidade de registro encontrados
     */
    @Override
    public long count() {
        return super.count();
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param spec filtro a ser utilizado na pesquisa
     * @param sort a ordem que voce deseja retornar o dados
     * @return resultado da pesquisa
     */
    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {

        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec, sort);
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param spec filtro a ser utilizado na pesquisa
     * @param pageable paginação
     * @return resultado da pesquisa
     */
    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec, pageable);
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param spec filtro a ser utilizado na pesquisa
     * @return resultado da pesquisa
     */
    @Override
    public List<T> findAll(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(spec);
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param spec filtro a ser utilizado na pesquisa
     * @return resultado da pesquisa
     */
    @Override
    public T findOne(Specification<T> spec) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findOne(spec);
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param pageable parametros da paginação
     * @return resultado da pesquisa
     */
    @Override
    public Page<T> findAll(Pageable pageable) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(pageable);
    }

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @param sort ordem de retorno dos dados
     * @return resultado da pesquisa
     */
    @Override
    public List<T> findAll(Sort sort) {
        if (hasMultitenancy()) {
            throw new GumgaGenericRepositoryException(noMultiTenancyMessage());
        }
        return super.findAll(sort);
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

    /**
     * Pesquisa na base de dados baseado na entidade tipada na interface
     *
     * @{@link GumgaCrudRepository} com os filtro passados
     *
     * @return resultado da pesquisa
     */
    @Override
    public List<T> findAll() {
        if (hasMultitenancy()) {
            log.error("NOMULTITENACYIMPL -> " + noMultiTenancyMessage());
        }
        return super.findAll();
    }

    /**
     * Verificar se objecto salvo no banco ja existe.
     *
     * @param id valor a ser pequisado na primary key da entidade
     * @return resultado da pesquisa
     */
    @Override
    public boolean exists(ID id) {
        return super.exists(id);
    }

    /**
     * Pesquisar a entidade tipada na interface @{@link GumgaCrudRepository} com
     * Multitenancy caso a entidade esteja anotada com
     *
     * @{@link GumgaMultitenancy}
     *
     * @param id
     * @return resultado da pesquisa
     */
    @Override
    public T getOne(ID id) {
        T objectFind = super.getOne(id);
        checkOwnership(objectFind);
        return objectFind;
    }

    @Override
    public void deleteAllInBatch() {
        if (hasMultitenancy()) {
            log.error("NOMULTITENACYIMPL -> " + noMultiTenancyMessage());
        }
        super.deleteAllInBatch();
    }

    /**
     * Remove todas as entidades tipada na interface
     *
     * @{@link GumgaCrudRepository} da base dados
     */
    @Override
    public void deleteAll() {
        if (hasMultitenancy()) {
            log.error("NOMULTITENACYIMPL -> " + noMultiTenancyMessage());
        }
        super.deleteAll();
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
    public void delete(Iterable<? extends T> entities) {
        if (hasMultitenancy()) {
            for (T entity : entities) {
                delete(entity);
            }
        }
        super.delete(entities);
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
            if (entity instanceof GumgaLDModel) {
                ((GumgaLDModel) entity).setGumgaActive(Boolean.FALSE);
                return;
            }
        }
        super.delete(entity);
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
        super.delete(id);
    }

    @Override
    protected Class<T> getDomainClass() {
        return super.getDomainClass();
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        super.setRepositoryMethodMetadata(crudMethodMetadata);
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

        if(o instanceof GumgaModel) {
            gumgaModel((GumgaModel) o);
        } else {
            if(o instanceof GumgaModelUUID) {
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

    @Override
    public SearchResult<T> findAllWithTenancy() {
        QueryObject qo = new QueryObject();
        qo.setPageSize(Integer.MAX_VALUE - 1);
        return search(qo);
    }

    private String getWhereMultiTenancy() {
        String tenant = " where ";

        if (hasMultitenancy() && GumgaThreadScope.organizationCode.get() != null && (GumgaThreadScope.ignoreCheckOwnership.get() == null || !GumgaThreadScope.ignoreCheckOwnership.get())) {
            String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(entityInformation.getJavaType().getAnnotation(GumgaMultitenancy.class));
            String oi = "obj.oi is null or obj.oi like '" + oiPattern + "%'";
            if (GumgaSharedModel.class.isAssignableFrom(entityInformation.getJavaType()) || GumgaSharedModelUUID.class.isAssignableFrom(entityInformation.getJavaType())) {
                String instanceOi = GumgaThreadScope.instanceOi.get() + GumgaSharedModel.GLOBAL;

                tenant = tenant.concat("(".concat(oi)).concat(" or obj.gumgaOrganizations like '%," + oiPattern + ",%' or "
                        + "obj.gumgaOrganizations like '%," + instanceOi + ",%' or "
                        + "obj.gumgaUsers like '%," + GumgaThreadScope.login.get() + ",%') ");
            } else {
                tenant = tenant.concat("(").concat(oi).concat(")");
            }
        } else {
            tenant = tenant.concat(" 1=1");
        }
        return tenant;
    }

    public SearchResult<T> findByGQuery(QueryObject queryObject) {
        if (queryObject.getgQuery() == null) {
            queryObject.setgQuery(new GQuery());
        }
        GQuery gQuery = queryObject.getgQuery();


        Query queryCountWithGQuery = createQueryCountWithGQuery(gQuery);
        Long total = (Long) queryCountWithGQuery.getSingleResult();

        Query queryWithGQuery = createQueryGQueryWithQueryObject(queryObject);

        queryWithGQuery.setMaxResults(queryObject.getPageSize());
        queryWithGQuery.setFirstResult(queryObject.getStart());

        return new SearchResult(queryObject, total, queryWithGQuery.getResultList());
    }

    @Override
    public T fetchOne(GQuery gQuery) {
        Query search = createQueryWithGQuery(gQuery);
        try {
            List<T> resultList = search.getResultList();
            return resultList.isEmpty() ? null : resultList.get(0);
//            return singleResult != null ? (T) singleResult : null;
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


    private Query createQueryGQueryWithQueryObject(QueryObject queryObject) {
        if(queryObject.getgQuery() == null) {
            queryObject.setgQuery(new GQuery());
        }
        GQuery gQuery = queryObject.getgQuery();

//        String sortDir = queryObject.getSortDir();
//        String sortField = queryObject.getSortField();
//        String sort = "obj.id asc";
//        if(!sortField.isEmpty()) {
//            sort = sortField + ("asc".equals(sortDir) ? " asc" : " desc");
//        }
        String sort = getOrderField(queryObject.getSortField(), queryObject.getSortDir());

        String query = "select distinct obj FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where).concat(" order by ").concat(sort));
    }
    private void getOrderField(Pesquisa<T> pesquisa, String sortField, String sorDir) {
        String orderColumns = sortField;
        String orderType = sorDir;

        String[] columns = null;
        if(orderColumns.indexOf(",") > 0) {
            columns = orderColumns.split(",");
        } else {
            columns = new String[]{orderColumns};
        }

        String[] types = null;
        if(orderType.indexOf(",") > 0) {
            types = orderType.split(",");
        } else {
            types = new String[]{orderType};
        }

        Boolean existsID = Boolean.FALSE;
        for (int i = 0; i < columns.length; i++) {
            pesquisa.addOrder(i < types.length && !types[i].trim().isEmpty() && types[i].trim().equalsIgnoreCase("desc") ? desc(columns[i]) : asc(columns[i]));
            if(columns[i].trim().equalsIgnoreCase("id")) {
                existsID = Boolean.TRUE;
            }
        }

        if(!existsID) {
            pesquisa.addOrder(asc("id"));
        }
    }

    private String getOrderField(String sortField, String sorDir) {
        if(sortField != null && sorDir != null && !sortField.trim().isEmpty()) {
            String orderColumns = sortField;
            String orderType = sorDir;

            String[] columns = null;
            if(orderColumns.indexOf(",") > 0) {
                columns = orderColumns.split(",");
            } else {
                columns = new String[]{orderColumns};
            }

            String[] types = null;
            if(orderType.indexOf(",") > 0) {
                types = orderType.split(",");
            } else {
                types = new String[]{orderType};
            }

            String ordem = "";
            Boolean existsID = Boolean.FALSE;
            for (int i = 0; i < columns.length; i++) {
                ordem = ordem.concat(columns[i]).concat(i < types.length ? " ".concat(types[i].trim().isEmpty() ? "asc" : types[i]) : " asc").concat(",");
                if(columns[i].trim().equalsIgnoreCase("id") || columns[i].trim().equalsIgnoreCase("obj.id")) {
                    existsID = Boolean.TRUE;
                }
            }

            if(!existsID) {
                ordem = ordem.concat("obj.id asc,");
            }

           return ordem.substring(0, ordem.length() -1);
        }

        return "obj.id asc";
    }

    private Query createQueryWithGQuery(GQuery gQuery) {
        String query = "select distinct obj FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where));
    }

    private Query createQueryCountWithGQuery(GQuery gQuery) {
        String query = "select distinct count(obj) FROM ".concat(entityInformation.getEntityName()).concat(" obj");

        String where = createWhere(gQuery);

        return entityManager.createQuery(query.concat(gQuery.getJoins()).concat(where));
    }

    private String createWhere(GQuery gQuery) {
        String gQueryWhere = gQuery.toString();

        if (GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getMySqlLikeMap())) {
            gQueryWhere = removeFunctionToTimestamp(gQueryWhere)
                    .replaceAll("translate\\(", "")
                    .replaceAll(", 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç','AAAAAAAAEEEEIIOOOOOOUUUUCC'\\)", "");
        } else {
            if(GumgaQueryParserProvider.defaultMap.equals(GumgaQueryParserProvider.getH2LikeMap())){
                gQueryWhere = removeFunctionToTimestamp(gQueryWhere);
            }
        }

        return getWhereMultiTenancy().concat(StringUtils.isEmpty(gQueryWhere) ? "" : " and ".concat(gQueryWhere));
    }

    private String removeFunctionToTimestamp(String gQueryWhere) {
        gQueryWhere = gQueryWhere.replaceAll("to_timestamp\\(", "")
                .replaceAll(",'yyyy/MM/dd HH24:mi:ss'\\)", "");
        return gQueryWhere;
    }

}
