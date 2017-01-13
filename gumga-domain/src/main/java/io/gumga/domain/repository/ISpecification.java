package io.gumga.domain.repository;

import com.mysema.query.jpa.JPQLQuery;

/**
 * Interface para consultas mais complexas
 */
@FunctionalInterface
public interface ISpecification {

    JPQLQuery createQuery(JPQLQuery query);

}
