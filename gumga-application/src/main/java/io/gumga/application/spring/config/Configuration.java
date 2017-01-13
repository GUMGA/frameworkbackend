package io.gumga.application.spring.config;

import io.gumga.core.GumgaValues;
import io.gumga.core.exception.TemplateEngineException;
import io.gumga.core.service.GumgaAbstractTemplateEngineAdapter;
import io.gumga.core.service.GumgaPasswordService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Interface which describes the common configurations for a gumga application.
 * 
 * @author gyowanny
 * @since jul/2015
 */
public interface Configuration{

    public JavaMailSender javaMailSenderImpl();

    public MethodValidationPostProcessor methodValidationPostProcessor(LocalValidatorFactoryBean validator);

    public LocalValidatorFactoryBean validator();

    public GumgaPasswordService gumgaPasswordService();

    public GumgaAbstractTemplateEngineAdapter templateEngineService(GumgaValues values) throws TemplateEngineException;
	
}
