package io.gumga.application;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gumga.application.GumgaRepositoryFactoryBean;
import io.gumga.application.service.GumgaFreemarkerTemplateEngineService;
import io.gumga.application.service.JasyptGumgaPasswordService;
import io.gumga.core.GumgaValues;
import io.gumga.core.exception.TemplateEngineException;
import io.gumga.core.service.GumgaPasswordService;
import io.gumga.domain.GumgaQueryParserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan({"io.gumga"})
@EnableJpaRepositories(repositoryFactoryBeanClass = GumgaRepositoryFactoryBean.class, basePackages = {"io.gumga"})
@EnableTransactionManagement(proxyTargetClass = true)
public class SpringConfig {

//    @Bean
//    public static DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder().setType(H2).build();
//    }

    @Bean(name = "dataSource")
    public static DataSource getDataSource() {
        return new HikariDataSource(generateConfigurationDataSource());
    }

    private static HikariConfig generateConfigurationDataSource() {
        HikariConfig configuration = new HikariConfig();
//        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getMySqlLikeMap();
//        configuration.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//        configuration.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/testeFramework?zeroDateTimeBehavior=convertToNull&useUnicode=yes&characterEncoding=utf8");
//        configuration.addDataSourceProperty("user", "root");
//        configuration.addDataSourceProperty("password", "senha");
        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getH2LikeMap();
        configuration.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        configuration.addDataSourceProperty("url", "jdbc:h2:mem:security;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        configuration.addDataSourceProperty("user", "sa");
        configuration.addDataSourceProperty("password", "");
        configuration.setMaximumPoolSize(20);
        configuration.setIdleTimeout(180000);
        configuration.setMaxLifetime(1800000);
        configuration.setInitializationFailFast(true);
        return configuration;
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        Properties properties = new Properties();
        properties.put("eclipselink.weaving", "false");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.connection.charSet", "UTF-8");
        properties.put("hibernate.connection.characterEncoding", "UTF-8");
        properties.put("hibernate.connection.useUnicode", "true");
        properties.put("hibernate.jdbc.batch_size", "50");
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(new String[]{"io.gumga"});
        factory.setDataSource(dataSource);
        factory.setJpaProperties(properties);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public GumgaPasswordService gumgaPasswordService() {
        return new JasyptGumgaPasswordService();
    }

    @Bean
    @Autowired
    public GumgaFreemarkerTemplateEngineService gumgaFreemarkerTemplateEngineService(GumgaValues gumgaValues) throws TemplateEngineException {
        GumgaFreemarkerTemplateEngineService service = new GumgaFreemarkerTemplateEngineService(gumgaValues.getTemplatesFolder(), "UTF-8");
        service.init();
        return service;
    }
}
