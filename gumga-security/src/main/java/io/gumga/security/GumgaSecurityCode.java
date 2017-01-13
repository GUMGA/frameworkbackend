/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.security;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author munif
 */
public enum GumgaSecurityCode {

    OK(HttpStatus.OK),
    ALLOW(HttpStatus.OK),
    UNDEFINED(HttpStatus.I_AM_A_TEAPOT),
    NO_USER(HttpStatus.UNAUTHORIZED),
    INACTIVE_USER(HttpStatus.UNAUTHORIZED),
    BLANK_PASSWORD(HttpStatus.UNAUTHORIZED),
    WEAK_PASSWORD(HttpStatus.UNAUTHORIZED),
    DIFFERENT_PASSWORD_REQUIRED(HttpStatus.UNAUTHORIZED),
    BAD_PASSWORD(HttpStatus.UNAUTHORIZED),
    NO_INSTANCE(HttpStatus.FORBIDDEN),
    NO_TOKEN(HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN),
    NO_ORGANIZATION(HttpStatus.FORBIDDEN),
    OPERATION_NOT_ALLOWED(HttpStatus.UNAUTHORIZED),
    INSTANCE_EXPIRED(HttpStatus.FORBIDDEN),
    IP_NOT_ALLOWED(HttpStatus.FORBIDDEN),
    TIME_NOT_ALLOWED(HttpStatus.FORBIDDEN),
    SECURITY_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    NO_SOFTWARE(HttpStatus.FORBIDDEN);

    public final HttpStatus httpStatus;

    GumgaSecurityCode(HttpStatus s) {
        httpStatus = s;
    }

    public Map response() {
        Map toReturn = new HashMap();
        toReturn.put("response", this);
        return toReturn;
    }

}
