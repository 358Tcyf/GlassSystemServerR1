package project.ys.glass_system.config.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryTertiary",
        transactionManagerRef = "transactionManagerTertiary",
        basePackages = {"project.ys.glass_system.model.t.dao"}) //设置Repository所在位置
public class TertiaryConfig {

    @Autowired
    @Qualifier("tertiaryDataSource")
    private DataSource tertiaryDataSource;

    @Bean(name = "entityManagerTertiary")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryTertiary(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryTertiary")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryTertiary(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(tertiaryDataSource)
                .properties(getVendorProperties())
                .packages("project.ys.glass_system.model.t.entity") //设置实体类所在位置
                .persistenceUnit("tertiaryPersistenceUnit")
                .build();
    }

    @Resource
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties() {
        return jpaProperties.getProperties();
    }

    @Bean(name = "transactionManagerTertiary")
    PlatformTransactionManager transactionManagerTertiary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryTertiary(builder).getObject());
    }

}