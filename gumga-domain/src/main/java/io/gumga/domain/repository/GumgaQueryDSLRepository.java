package io.gumga.domain.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import io.gumga.core.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Cria um repositório Tipado para QueryDSL
 *
 * @author munif
 */
@NoRepositoryBean
public interface GumgaQueryDSLRepository<T, ID extends Serializable> extends GumgaCrudRepository<T, ID>, QueryDslPredicateExecutor<T> {

    List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    List<T> findAll(Predicate specification);

    List<T> findAll(ISpecification specification);

    Page<T> findAll(ISpecification specification, Pageable page);

    <A> List<A> findAll(ISpecification specification, Expression<A> projection);

    <A> Page<A> findAll(ISpecification specification, Pageable page, Expression<A> projection);

    SearchResult<T> search(Predicate predicate, Pageable page);

    SearchResult<T> search(ISpecification specification, Pageable page);

    <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection);

    <A> SearchResult<A> search(ISpecification specification, Pageable page, Expression<A> projection);

    <A> A findOne(ISpecification query, Expression<A> projection);

}
