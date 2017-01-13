package io.gumga.domain;

import org.hibernate.criterion.Criterion;

/**
 * Apenas uma interface para faclitar permitir a escolha
 *
 * @author munif
 */
@FunctionalInterface
public interface CriterionParser {

    Criterion parse(String field, String value);
}
