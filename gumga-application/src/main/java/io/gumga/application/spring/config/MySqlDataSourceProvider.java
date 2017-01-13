package io.gumga.application.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gumga.domain.GumgaQueryParserProvider;

import javax.sql.DataSource;

/**
 * Data source for the MySql database
 *
 * @author gyowanny
 *
 */
public class MySqlDataSourceProvider implements DataSourceProvider {

    @Override
    public DataSource createDataSource(String url, String user, String password) {
        return createDataSource(url, user, password, 5, 20);
    }

    @Override
    public DataSource createDataSource(String url, String user, String password, int minConnections, int maxConnections) {
        initDefaultMap();
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        config.setMinimumIdle(minConnections);
        config.setMaximumPoolSize(maxConnections);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);

        return new HikariDataSource(config);
    }

    @Override
    public String getDialect() {
        return "org.hibernate.dialect.MySQLDialect";
    }

    public static synchronized void initDefaultMap() {
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getMySqlLikeMap();
    }

}
