package io.gumga.domain.support;

import io.gumga.domain.GumgaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Classe utilitária para vaçidações
 */
public class EntityValidatorSupport {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Logger logger = LoggerFactory.getLogger(EntityValidatorSupport.class);

    public static void assertEntidadeConsistente(GumgaModel<Long> resource) {
        Set<ConstraintViolation<GumgaModel<Long>>> violations = validator.validate(resource);
        logger.info(violations.toString());

        if (!violations.isEmpty()) {
            throw new AssertionError("Entity is not consistent! " + violations.size() + " violations found!");
        }
    }

    public static void assertContainsViolation(String field, String violation, GumgaModel<Long> resource) {
        Set<ConstraintViolation<GumgaModel<Long>>> violations = validator.validate(resource);
        logger.info(violations.toString());

        for (ConstraintViolation<GumgaModel<Long>> constraintViolation : violations) {
            if (field.equals(constraintViolation.getPropertyPath().toString())) {
                if (violation.equals(constraintViolation.getMessage())) {
                    return;
                }
            }
        }

        throw new AssertionError("não foi encontrada a validação \"" + violation + "\" para o campo " + "\"" + field + "\"");
    }

}
