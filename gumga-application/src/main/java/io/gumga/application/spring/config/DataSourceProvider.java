package io.gumga.application.spring.config;

import javax.sql.DataSource;

/**
 * Interface describing the behavior of an data source provider.
 * There should be one implementation per database type.
 * 
 * @author gyowanny
 * @since jul/2015
 * 
 */
public interface DataSourceProvider {

	/**
	 * Creates the data source
         * @param url The url to access the database
         * @param user database user
         * @param password database password
	 * @return a data source instance
	 */
	public DataSource createDataSource(String url, String user, String password);
	
        /**
         * Creates the data source with additional parameters
         * @param url The url to access the database
         * @param user database user
         * @param password database password
         * @param minConnections The minimum database connections to be open by the connection pool
         * @param maxConnections The maximum database connections to be open by the connection pool
         * @return a data source instance
         */
        public DataSource createDataSource(String url, String user, String password, int minConnections, int maxConnections);
        
	/**
	 * Returns the name of the dialect
	 * @return
	 */
	public String getDialect();
}
