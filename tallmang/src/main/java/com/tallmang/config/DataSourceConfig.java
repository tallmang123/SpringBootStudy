package com.tallmang.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.tallmang.repository")
@MapperScan(basePackages = {"com.tallmang.mybatis.mapper"})
@EnableTransactionManagement

public class DataSourceConfig {
	
	/*=========================DatatSource============================*/
	/**
	 * HikariDataSource Builder
	 * @return
	 */
	@Bean(name="mysqlDataSource",destroyMethod="close")
	@Primary
	@ConfigurationProperties(prefix ="spring.datasource.hikari")
	public DataSource mybatisDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	/*=========================DatatSource============================*/
	
	/*=========================MYBATIS============================*/
	/**
	 * Mybatis SqlSessionFactory : connect mysql datasource and mybatis 
	 * @param mybatisDataSource
	 * @param applicationContext
	 * @return
	 * @throws Exception
	 */
	@Bean(name="mybatisSqlSessionFactory")
	@Primary
	public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mysqlDataSource")DataSource mybatisDataSource, ApplicationContext applicationContext) throws Exception
	{
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(mybatisDataSource);
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/*.xml"));
		sessionFactoryBean.setTypeAliasesPackage("com.tallmang.mybatis.vo");
		return sessionFactoryBean.getObject();
	}

	/**
	 * Mybatis SqlSessionTemplate
	 * @param sqlSessionFactory
	 * @return
	 * @throws Exception
	 */
	@Bean(name="mybatisSqlSessionTemplate")
	public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory")SqlSessionFactory sqlSessionFactory) throws Exception 
	{
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}
	
	/*=========================MYBATIS============================*/

	/*=========================JPA===============================*/
	/**
	 * EntityManagerFactory
	 * 
	 * 
	 * Bean Name : default bean name value is 'entityManagerFactory' 
	 * But if you doesn't have to fix this value , you see error that can not find entityManagerFactory bean. it needs to figure out why it has to
	 * @param dataSource
	 * @return
	 */
	// 
	@Bean(name="entityManagerFactory") 	 
	@Primary
	public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(@Qualifier("mysqlDataSource") DataSource dataSource) 
	{
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPackagesToScan("com.tallmang.entity");
		emf.setJpaVendorAdapter(hibernateJpaVendorAdapter());
		
		return emf;
	}
	
	/**
	 * need to jpa provider ( ->hibernate) 
	 * @return
	 */
	@Bean 
	public HibernateJpaVendorAdapter hibernateJpaVendorAdapter() 
	{ 
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter(); 
		hibernateJpaVendorAdapter.setShowSql(true); 
		return hibernateJpaVendorAdapter; 
	}
	/*=========================JPA===============================*/
	
	/**
	 * If use annotaion Transactional, PlatformTransactionManger have to set only one although you use mutiple orm service. 
	 * (or always specify which transaction to use by name attribute)
	 * Jpa use JpaTransactionManager class , Mybatis use DataSourceTransactionManager class
	 * so use ChainedTransactionManager class to merge both of transactionManager
	 * 
	 *  
	 * Bean Name : default name value is 'transactionManager' 
	 * But if you doesn't have to fix this value , you see can not find transactionManager bean. it Needs to figure out why it has to
	 * 
	 * @param entityManagerFactory
	 * @param mybatisDataSource
	 * @return
	 */
	@Bean(name="transactionManager") 
    public PlatformTransactionManager jpaTransactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory, @Qualifier("mysqlDataSource")DataSource mybatisDataSource) 
	{
		//JPA Transaction
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        
        //Mybatis Transaction
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(mybatisDataSource);
        dataSourceTransactionManager.setGlobalRollbackOnParticipationFailure(false);
	    
		// creates chained transaction manager
		ChainedTransactionManager transactionManager = new ChainedTransactionManager(jpaTransactionManager, dataSourceTransactionManager);
		return transactionManager;

    }
}
