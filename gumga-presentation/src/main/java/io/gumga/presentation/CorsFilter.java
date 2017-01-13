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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author munif
 */
@WebFilter(filterName = "CorsFilter", urlPatterns = {"/*"},asyncSupported = true)
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {
        Logger.getLogger(CorsFilter.class.getName()).log(Level.INFO,"Gumga CORS FILTER ENABLED");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS,HEAD");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, gumgaToken, Connection, userRecognition");
        fc.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}




// 'Access-Control-Request-Method: GET' -H 'Access-Control-Request-Headers: gumgatoken' -H 'Connection: keep-alive'