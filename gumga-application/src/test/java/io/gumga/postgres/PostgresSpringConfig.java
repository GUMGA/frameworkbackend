package io.gumga.postgres;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.gumga.application.*;
import io.gumga.domain.CriterionParser;
import io.gumga.domain.GumgaQueryParserProvider;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.orm.jpa.vendor.Database;

@Configuration
@ComponentScan({"io.gumga.testmodel","io.gumga.application.customfields","io.gumga.core"})
@EnableJpaRepositories(repositoryFactoryBeanClass = GumgaRepositoryFactoryBean.class, basePackages = {"io.gumga.testmodel","io.gumga.application.customfields","io.gumga.core"})
@EnableTransactionManagement(proxyTargetClass = true)
public class PostgresSpringConfig {
   
    
    private HikariConfig commonConfig() {

        GumgaQueryParserProvider.defaultMap = GumgaQueryParserProvider.getPostgreSqlLikeMap();
        HikariConfig config = new HikariConfig();
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setIdleTimeout(30000L);
        config.setInitializationFailFast(true);
        config.setDataSourceClassName("org.postgresql.jdbc2.optional.SimpleDataSource");
        config.addDataSourceProperty("url", "jdbc:postgresql://localhost:5432/gumga_test");
        config.addDataSourceProperty("user", "gumga_test");
        config.addDataSourceProperty("password", "qwe123");
        return config;
    }

    private Properties commonProperties() {
        Properties properties = new Properties();
        properties.setProperty("eclipselink.weaving", "false");
        properties.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.EJB3NamingStrategy");
        properties.setProperty("hibernate.show_sql",  "true");
        properties.setProperty("hibernate.format_sql", "false");
        properties.setProperty("hibernate.connection.charSet", "UTF-8");
        properties.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        properties.setProperty("hibernate.connection.useUnicode", "true");
        properties.setProperty("hibernate.jdbc.batch_size", "50");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }

    @Bean
    public DataSource dataSource() {

        HikariConfig config = commonConfig();
        return new HikariDataSource(config);
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("io.gumga.domain", "io.gumga.testmodel");
        factory.setDataSource(dataSource);
        factory.setJpaProperties(commonProperties());
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
