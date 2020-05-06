package com.tallmang.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.tallmang.repository")
@EnableTransactionManagement
public class JpaConfig {
	
	@Bean(name="jpaDataSource",destroyMethod="close")
	@Primary
	@ConfigurationProperties(prefix ="spring.datasource.hikari")
	public DataSource mybatisDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	@Bean(name="entityManagerFactory") // bean name have to fix 
	@Primary
	public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(@Qualifier("jpaDataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPackagesToScan("com.tallmang.entity");
		emf.setJpaVendorAdapter(hibernateJpaVendorAdapter());
		
		return emf;
	}
	
	@Bean(name="transactionManager") // bean name have to fix 
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);

        return transactionManager;
    }
	
	//need to jpa provider ( ->hibernate)
	@Bean 
	public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() { 
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter(); 
		hibernateJpaVendorAdapter.setShowSql(true); 
		return hibernateJpaVendorAdapter; 
	}

}
