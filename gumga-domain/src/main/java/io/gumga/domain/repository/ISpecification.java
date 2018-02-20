package io.gumga.domain.repository;

import com.mysema.query.jpa.JPQLQuery;

/**
 * Interface para consultas mais complexas
 * @see <a href="http://www.querydsl.com/static/querydsl/4.0.0/apidocs/com/querydsl/jpa/JPQLQuery.html">JPQLQuery</a>
 */
@FunctionalInterface
public interface ISpecification {

    JPQLQuery createQuery(JPQLQuery query);

}
