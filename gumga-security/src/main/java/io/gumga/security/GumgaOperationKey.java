/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author munif
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GumgaOperationKey {

    public String value();

}
