package io.gumga.security;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface para implementação dos métodos onde serão mapeados as chaves de controle das requisições e determinadas URLs
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
