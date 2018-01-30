package io.gumga.application.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gumga.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

/**
 * Data source for the PostgreSQL database
 */
public class PostgreSqlDataSourceProvider implements DataSourceProvider {
    /**
     * Create PostgreSQL datasource with fixed number of connections (minConnections: 5. maxConnections: 20)
     * @param url The url to access the database
     * @param user database user
     * @param password database password
     * @return
     */
    @Override
    public DataSource createDataSource(String url, String user, String password) {
        return createDataSource(url, user, password, 5, 20);
    }

    /**
     * Create data source for PostgreSQL database
     * @param url The url to access the database
     * @param user database user
     * @param password database password
     * @param minConnections The minimum database connections to be open by the connection pool
     * @param maxConnections The maximum database connections to be open by the connection pool
     * @return
     */
    @Override
    public DataSource createDataSource(String url, String user, String password, int minConnections, int maxConnections) {
        initDefaultMap();
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName("org.postgresql.jdbc2.optional.SimpleDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);
        return new HikariDataSource(config);
    }
    /**
     * Get the dialect for PostgreSQL database
     * @return Dialect for PostgreSQL database
     */
    @Override
    public String getDialect() {
        return "org.hibernate.dialect.PostgreSQLDialect";
    }
    /**
     * Set the Map to use PostgreSQL Database
     */
    public static synchronized void initDefaultMap() {
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getPostgreSqlLikeMap();
    }

}
