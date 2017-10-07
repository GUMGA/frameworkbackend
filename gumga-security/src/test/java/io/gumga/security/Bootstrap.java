package io.gumga.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class Bootstrap extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    public Bootstrap() {
        log.info("INICIO");
    }
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{};//Application.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

}
