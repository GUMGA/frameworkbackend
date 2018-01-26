package io.gumga.domain.nlp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Anotação para tornar uma classe mapeável do GumgaNLP
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GumgaNLPThing {

    public String value() default "_NO_NAME";

}
