package io.gumga.application.spring.config;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Interface which describes the common configurations for a gumga application
 * with persistence.
 * 
 * @author gyowanny
 * @since jul/2015
 */
public interface PersistenceConfiguration {

	public PlatformTransactionManager transactionManager(EntityManagerFactory emf);
	
	public DataSource dataSource();
	
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource);
}
