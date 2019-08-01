package io.gumga.application.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gumga.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

/**
 * Data source provider for H2 database
 *
 * @author gyowanny
 *
 */
public class H2DataSourceProvider implements DataSourceProvider {

    @Override
    public DataSource createDataSource(String url, String user, String password) {
        return createDataSource(url, user, password, 5, 20);
    }

    /**
     * Create data source for H2 database
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
        config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        config.setIdleTimeout(30000L);
//        config.setInitializationFailFast(true);
        return new HikariDataSource(config);
    }

    /**
     * Get the dialect for H2 database
     * @return Dialect for H2 database
     */
    @Override
    public String getDialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    /**
     * Set the Map to use H2 Database
     */
    public static synchronized void initDefaultMap() {
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getH2LikeMap();
    }

}
