package com.radix.application.config.oracle;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        basePackages = "com.radix.infrastructure.persistence"
)
public class OracleDsConfig {

    @Primary
    @Bean(name = "oracleDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.oracle.datasource")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "oracleSource")
    @ConfigurationProperties(prefix = "spring.oracle.datasource")
    public DataSource dataSource(@Qualifier("oracleDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("oracleSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();

        // Jpa settings
        properties.put("spring.jpa.hibernate.naming.implicit-strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl");
        properties.put("spring.jpa.hibernate.naming.physical-strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
        properties.put("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        properties.put("spring.jpa.database-platform", "org.hibernate.dialect.Oracle12cDialect");

        // HikariCP settings
        properties.put("spring.datasource.hikari.minimumIdle", 5);
        properties.put("spring.datasource.hikari.maximumPoolSize", 20);
        properties.put("spring.datasource.hikari.idleTimeout", 30000);
        properties.put("spring.datasource.hikari.maxLifetime", 2000000);
        properties.put("spring.datasource.hikari.connectionTimeout", 30000);
        properties.put("spring.datasource.hikari.poolName", "HikariPoolBooks");

        //properties.put("hibernate.hbm2ddl.auto", "update");
        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.radix.infrastructure.persistence")
                .persistenceUnit("oracle")
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
