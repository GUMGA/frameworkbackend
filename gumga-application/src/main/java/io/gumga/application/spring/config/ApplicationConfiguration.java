package io.gumga.application.spring.config;

/**
 * Default interface used to configure the spring over annotations.
 * It supports common app configurations as well as database ones.
 * 
 * @author gyowanny
 * @since jul/2015
 */
public interface ApplicationConfiguration extends Configuration,
		PersistenceConfiguration {

}
