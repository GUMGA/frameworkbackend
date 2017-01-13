package io.gumga.security;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author munif
 */
public interface ApiOperationTranslator {

    default String getOperation(String url, String method) {
        return "NOOP";
    }

    default String getOperation(String url, String method, HttpServletRequest request) {
        return getOperation(url, method);
    }

}
