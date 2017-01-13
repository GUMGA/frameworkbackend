package io.gumga.application.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author gyowanny
 *
 */
public class DatabaseConfigSupport {

    /**
     *
     * @author gyowanny
     *
     */
    public static enum Database {
        H2, MYSQL, POSTGRES, ORACLE
    }

    private static final Map<Database, DataSourceProvider> dsProviderMap = new HashMap<>();

    {
        dsProviderMap.put(Database.H2, new H2DataSourceProvider());
        dsProviderMap.put(Database.MYSQL, new MySqlDataSourceProvider());
        dsProviderMap.put(Database.POSTGRES, new PostgreSqlDataSourceProvider());
        dsProviderMap.put(Database.ORACLE, new OracleDataSourceProvider());
    }

    public DatabaseConfigSupport() {

    }

    public DataSourceProvider getDataSourceProvider(String dbStr) {
        Database db = Database.valueOf(dbStr.toUpperCase());
        return getDataSourceProvider(db);
    }

    public DataSourceProvider getDataSourceProvider(Database db) {
        if (db == null) {
            return null;
        } else {
            return dsProviderMap.get(db);
        }
    }

    public void registerDataSourceProvider(Database db, DataSourceProvider dspImpl) {
        dsProviderMap.put(db, dspImpl);
    }

    public Properties getDefaultHibernateProperties(DataSourceProvider provider) {
        Properties properties = new Properties();
        properties.put("eclipselink.weaving", "false");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", provider.getDialect());
        //properties.put("hibernate.ejb.naming_strategy", "br.com.gumga.security.infrastructure.GumgaNamingStrategy");
        properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.put("hibernate.connection.charSet", "UTF-8");
        properties.put("hibernate.connection.characterEncoding", "UTF-8");
        properties.put("hibernate.connection.useUnicode", "true");
        properties.put("hibernate.jdbc.batch_size", "50");
        return properties;
    }

    public Properties getDefaultEclipseLinkProperties(DataSourceProvider provider) {
        Properties properties = new Properties();
        return properties;
    }

    public Properties getDefaultOpenJPAProperties(DataSourceProvider provider) {
        Properties properties = new Properties();
        return properties;
    }
}
