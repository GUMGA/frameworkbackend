package io.gumga.security;

import java.util.logging.Level;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class Bootstrap extends AbstractAnnotationConfigDispatcherServletInitializer {

    public Bootstrap() {
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"INICIO");
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
