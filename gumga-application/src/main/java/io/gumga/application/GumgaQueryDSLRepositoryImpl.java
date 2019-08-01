package io.gumga.application;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import io.gumga.core.SearchResult;
import io.gumga.domain.repository.GumgaQueryDSLRepository;
import io.gumga.domain.repository.ISpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
@Deprecated
public class GumgaQueryDSLRepositoryImpl<T, ID extends Serializable> extends GumgaGenericRepository<T, ID> implements GumgaQueryDSLRepository<T, ID> {

    public GumgaQueryDSLRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return null;
    }

    @Override
    public Iterable<T> findAll(OrderSpecifier<?>... orders) {
        return null;
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return null;
    }

    @Override
    public long count(Predicate predicate) {
        return 0;
    }

    @Override
    public boolean exists(Predicate predicate) {
        return false;
    }

    @Override
    public Optional<T> findOne(Predicate predicate) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll(Predicate specification) {
        return null;
    }

    @Override
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return null;
    }

    @Override
    public List<T> findAll(ISpecification specification) {
        return null;
    }

    @Override
    public Page<T> findAll(ISpecification specification, Pageable page) {
        return null;
    }

    @Override
    public <A> List<A> findAll(ISpecification specification, Expression<A> projection) {
        return null;
    }

    @Override
    public <A> Page<A> findAll(ISpecification specification, Pageable page, Expression<A> projection) {
        return null;
    }

    @Override
    public SearchResult<T> search(Predicate predicate, Pageable page) {
        return null;
    }

    @Override
    public SearchResult<T> search(ISpecification specification, Pageable page) {
        return null;
    }

    @Override
    public <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection) {
        return null;
    }

    @Override
    public <A> SearchResult<A> search(ISpecification specification, Pageable page, Expression<A> projection) {
        return null;
    }

    @Override
    public <A> A findOne(ISpecification query, Expression<A> projection) {
        return null;
    }

//    private final EntityPath<T> path;
//    private final Querydsl querydsl;
//
//    public GumgaQueryDSLRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
//        super(entityInformation, entityManager);
//
//        this.path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
//        PathBuilder<T> builder = new PathBuilder<>(path.getType(), path.getMetadata());
//        this.querydsl = new Querydsl(entityManager, builder);
//    }
//
//    public T findOne(Predicate predicate) {
//        return createQuery(predicate).uniqueResult(path);
//    }
//
//    /**
//     * Pesquisa todos os registro da entidade tipada na classe {@link GumgaQueryDSLRepositoryImpl}
//     * @return dados da pesquisa
//     */
//    public List<T> findAll() {
//        return createQuery().list(path);
//    }
//
//    /**
//     * Pesquisa todos os registro da entidade tipada na classe {@link GumgaQueryDSLRepositoryImpl}
//     * @param predicate filtro da pesquisa
//     * @return dados da pesquisa
//     */
//    public List<T> findAll(Predicate predicate) {
//        return findAll(toSpecification(predicate), path);
//    }
//
//    @Override
//    public Iterable<T> findAll(Predicate predicate, Sort sort) {
//        return null;
//    }
//
//    /**
//     * Pesquisa todos os registro da entidade tipada na classe {@link GumgaQueryDSLRepositoryImpl}
//     * @param predicate filtro da pesquisa
//     * @param orders   ordem dos dados
//     * @return dados da pesquisa
//     */
//    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
//        JPQLQuery query = createQuery(predicate);
//        query = querydsl.applySorting(new QSort(orders), query);
//        return query.list(path);
//    }
//
//    @Override
//    public Iterable<T> findAll(OrderSpecifier<?>[] orderSpecifiers) {
//        return null;
//    }
//
//    @Override
//    public List<T> findAll(ISpecification specification) {
//        return findAll(specification, path);
//    }
//
//    @Override
//    public <A> List<A> findAll(ISpecification specification, Expression<A> projection) {
//        return createQuery(specification).list(projection);
//    }
//
//    @Override
//    public Page<T> findAll(ISpecification specification, Pageable page) {
//        return findAll(specification, page, path);
//    }
//
//    @Override
//    public <A> Page<A> findAll(ISpecification specification, Pageable page, Expression<A> projection) {
//        long total = createQuery(specification).count();
//
//        JPQLQuery query = createQuery(specification);
//        query = querydsl.applyPagination(page, query);
//        List<A> content = total > page.getOffset() ? query.list(projection) : Collections.<A>emptyList();
//
//        return new PageImpl<>(content, page, total);
//    }
//
//    @Override
//    public SearchResult<T> search(Predicate predicate, Pageable page) {
//        return search(toSpecification(predicate), page);
//    }
//
//    @Override
//    public <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection) {
//        return search(toSpecification(predicate), page, projection);
//    }
//
//    @Override
//    public SearchResult<T> search(ISpecification specification, Pageable page) {
//        return search(specification, page, path);
//    }
//
//    @Override
//    public <A> SearchResult<A> search(ISpecification specification, Pageable page, Expression<A> projection) {
//        return createResultFromPageResult(page, this.findAll(specification, page, projection));
//    }
//
//    @Override
//    public <A> A findOne(ISpecification specification, Expression<A> projection) {
//        return specification.createQuery(querydsl.createQuery(path)).uniqueResult(projection);
//    }
//
//    /**
//     * Pesquisa todos os registro da entidade tipada na classe {@link GumgaQueryDSLRepositoryImpl}
//     * @param predicate filtros da pesquisa
//     * @param pageable configurações de paginação
//     * @return dados da pesquisa
//     */
//    @Override
//    public Page<T> findAll(Predicate predicate, Pageable pageable) {
//        return findAll(toSpecification(predicate), pageable, path);
//    }
//
//    @Override
//    public long count(Predicate predicate) {
//        return createQuery(predicate).count();
//    }
//
//    @Override
//    public boolean exists(Predicate predicate) {
//        return false;
//    }
//
//    private <A> SearchResult<A> createResultFromPageResult(Pageable page, Page<A> result) {
//        return new SearchResult<>(page.getOffset(), page.getPageSize(), result.getTotalElements(), result.getContent());
//    }
//
//    /**
//     * Criar uma nova Jpaquery com os filtros e com multitenancy se a classe tiver anotada com {@link GumgaMultitenancy}
//     * @param predicate filtro dos dados
//     * @return dados da pesquisa
//     */
//    public JPQLQuery createQuery(Predicate... predicate) {
//        return createQuery(toSpecification(predicate));
//    }
//
//    private ISpecification toSpecification(Predicate... predicate) {
//        return query -> query.where(predicate);
//    }
//
//    private JPQLQuery createQuery(ISpecification specification) {
//        JPQLQuery query = specification.createQuery(querydsl.createQuery(path));
//
//        if (this.hasMultitenancy()) {
//            query.where(getOiExpression());
//        }
//
//        return query;
//    }
//
//    private BooleanExpression getOiExpression() {
//        GumgaMultitenancy tenancy = getDomainClass().getAnnotation(GumgaMultitenancy.class);
//        String oiPattern = GumgaMultitenancyUtil.getMultitenancyPattern(tenancy);
//        ComparablePath<GumgaOi> oi = new ComparablePath<>(GumgaOi.class, PathMetadataFactory.forProperty(this.path, "oi"));
//        if (tenancy.allowPublics()) {
//            if (tenancy.publicMarking().equals(TenancyPublicMarking.NULL)) {
//                oi.
//                return oi.stringValue().startsWith(oiPattern).or(oi.isNull());
//            }
//            return oi.stringValue().startsWith(oiPattern).or(oi.eq(GumgaOi.MARK_PUBLIC));
//        }
//        return oi.stringValue().startsWith(oiPattern);
//    }


}
