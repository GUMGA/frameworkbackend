/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.domain;

import io.gumga.core.TenancyPublicMarking;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Esta é a anotação que "marca" as entidades como Multitenancy. Apenas as
 * entidades com esta anotação serão gerenciadas com Multitenancy
 *
 * @author munif
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GumgaMultitenancy {

    GumgaMultitenancyPolicy policy() default GumgaMultitenancyPolicy.TOP_DOWN;

    boolean allowPublics() default true;

    TenancyPublicMarking publicMarking() default TenancyPublicMarking.NULL;

    boolean enableBMO() default false;
}
