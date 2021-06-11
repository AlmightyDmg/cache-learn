package com.haizhi.databridge.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.haizhi.data.jpa.HaizhiStandardRepositoryImpl;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月03日 10:16:46
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        //实体管理
        entityManagerFactoryRef = "entityManagerFactorySecond",
        //事务管理
        transactionManagerRef = "transactionManagerSecond",
        //实体扫描,设置Repository所在位置
        basePackages = {"com.haizhi.databridge.repository.exportdata"},
        repositoryBaseClass = HaizhiStandardRepositoryImpl.class)
public class SecondConfig {
    private static final int INIT_CAPACITY = 16;

    @Autowired
    @Qualifier("secondDataSource")
    private DataSource secondDataSource;

    @Autowired
    private Environment env;

    @Bean(name = "entityManagerSecond")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactorySecond(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactorySecond")
    public LocalContainerEntityManagerFactoryBean entityManagerFactorySecond(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(secondDataSource)
                .properties(getVendorProperties())
                .packages("com.haizhi.databridge.bean.domain.exportdata")
                .persistenceUnit("secondPersistenceUnit")
                .build();
    }

    private Map<String, String> getVendorProperties() {
        Map<String, String> jpaProperties = new HashMap<>(INIT_CAPACITY);
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        jpaProperties.put("hibernate.dialect", env.getProperty("spring.jpa.hibernate.second-dialect"));
        jpaProperties.put("hibernate.format_sql", env.getProperty("spring.jpa.hibernate.format_sql"));
        jpaProperties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        return jpaProperties;
    }

    @Bean(name = "transactionManagerSecond")
    PlatformTransactionManager transactionManagerSecond(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactorySecond(builder).getObject());
    }
}
