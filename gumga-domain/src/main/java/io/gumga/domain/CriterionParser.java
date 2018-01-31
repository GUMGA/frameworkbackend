package io.gumga.domain;

import org.hibernate.criterion.Criterion;

/**
 * Apenas uma interface para facilitar permitir a escolha de critério de busca
 *
 * @author munif
 */
@FunctionalInterface
public interface CriterionParser {

    Criterion parse(String field, String value);
}
