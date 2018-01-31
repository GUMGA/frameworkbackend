/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.gumga.presentation;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Classe para manipulação de filtros nas requisições
 * @author munif
 */
@WebFilter(filterName = "CorsFilter", urlPatterns = {"/*"},asyncSupported = true)
public class CorsFilter implements Filter {
    
    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    /**
     * Inicializa o módulo
     * @param fc
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig fc) throws ServletException {
        log.info("Inicializando");
    }

    /**
     * Aplica filtros à requisição HTTP
     * @param req Objeto ServletRequest contendo a requisição
     * @param res Objeto ServletResponse contendo a resposta
     * @param fc Objeto FilterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS,HEAD");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, gumgaToken, Connection, userRecognition");
        fc.doFilter(request, response);
    }

    /**
     * Não implementado
     */
    @Override
    public void destroy() {

    }

}




// 'Access-Control-Request-Method: GET' -H 'Access-Control-Request-Headers: gumgatoken' -H 'Connection: keep-alive'