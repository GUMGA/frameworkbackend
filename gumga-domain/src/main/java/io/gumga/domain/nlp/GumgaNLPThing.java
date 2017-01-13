package io.gumga.domain.nlp;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GumgaNLPThing {

    public String value() default "_NO_NAME";

}
