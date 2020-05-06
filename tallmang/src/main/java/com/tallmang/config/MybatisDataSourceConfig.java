package com.tallmang.config;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

//@Configuration
//@MapperScan(basePackages = {"com.tallmang.mybatis.mapper"})
//@EnableTransactionManagement
public class MybatisDataSourceConfig {
	//set mysql datasource (hikari)
	/*@Bean(name="mybatisDataSource",destroyMethod="close")
	@Primary
	@ConfigurationProperties(prefix ="spring.datasource.hikari")
	public DataSource mybatisDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}
	
	//connect mysql datasource and mybatis 
	@Bean(name="mybatisSqlSessionFactory")
	@Primary
	public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mybatisDataSource")DataSource mybatisDataSource, ApplicationContext applicationContext) throws Exception
	{
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(mybatisDataSource);
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/*.xml"));
		sessionFactoryBean.setTypeAliasesPackage("com.tallmang.mybatis.vo");
		return sessionFactoryBean.getObject();
	}
	
    @Bean(name="mybatisSqlSessionTemplate")
    public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory")SqlSessionFactory sqlSessionFactory) throws Exception {
      SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
      return sqlSessionTemplate;
    }
	
	//setting to use transaction
    @Bean(name="mybatisTransactionManager")
    public PlatformTransactionManager mybatisTransactionManager(@Qualifier("mybatisDataSource")DataSource mybatisDataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(mybatisDataSource);
        transactionManager.setGlobalRollbackOnParticipationFailure(false);
        return transactionManager;
    }*/
}
