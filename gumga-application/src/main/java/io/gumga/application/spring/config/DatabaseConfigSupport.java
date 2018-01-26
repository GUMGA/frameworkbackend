package io.gumga.application.spring.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Classe com utilitários para configuração de conexão de banco de dados
 * @author gyowanny
 *
 */
public class DatabaseConfigSupport {

    /**
     * Bancos utilizados
     * @author gyowanny
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

    /**
     * Fonte de dados do banco utilizado
     * @param dbStr Nome do Banco {@link Database}
     * @return Provedor de Fonte de Dados
     */
    public DataSourceProvider getDataSourceProvider(String dbStr) {
        Database db = Database.valueOf(dbStr.toUpperCase());
        return getDataSourceProvider(db);
    }

    /**
     * Fonte de dados do banco utilizado
     * @param db Banco
     * @return Fonte de Dados
     */
    public DataSourceProvider getDataSourceProvider(Database db) {
        if (db == null) {
            return null;
        } else {
            return dsProviderMap.get(db);
        }
    }

    /**
     * Registra Fonte de dados
     * @param db Banco de dados
     * @param dspImpl Fonte de Dados
     */
    public void registerDataSourceProvider(Database db, DataSourceProvider dspImpl) {
        dsProviderMap.put(db, dspImpl);
    }

    /**
     * Objeto com propriedades da Fonte de dados
     * @param provider Fonte de dados
     * @return Objeto com propriedades
     */
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

    /**
     * Objeto de propriedade para EclipseLink
     * @see <a href="http://www.eclipse.org/eclipselink"/>EclipseLink</a>
     * @param provider Fonte de dados
     * @return Objeto com propriedades
     */
    public Properties getDefaultEclipseLinkProperties(DataSourceProvider provider) {
        Properties properties = new Properties();
        return properties;
    }

    /**
     * Objeto de propriedade para JPA
     * @see <a href="http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html"/>JPA</a>
     * @param provider Fonte de dados
     * @return Objeto com propriedades
     */
    public Properties getDefaultOpenJPAProperties(DataSourceProvider provider) {
        Properties properties = new Properties();
        return properties;
    }
}
