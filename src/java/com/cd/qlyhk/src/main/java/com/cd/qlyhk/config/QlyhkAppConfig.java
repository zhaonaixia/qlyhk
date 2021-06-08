package com.cd.qlyhk.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import com.cd.rdf.base.dao.IBaseDAO;
import com.cd.rdf.base.dao.impl.BaseDAOImpl;
import com.cd.rdf.cache.redis.RedisService;

@Configuration
public class QlyhkAppConfig {

  @Value("${spring.redis.host}")
  private String redisHost;
  @Value("${spring.redis.port}")
  private String redisPort;
  @Value("${spring.redis.timeout}")
  private int redisTimeoutMs;
  @Value("${spring.redis.password}")
  private String redisPassword;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private SqlSessionTemplate sqlSessionTemplate;

//  @Autowired
//  private SqlSessionFactoryBean sqlSessionFactoryBean;
  
  @Autowired
  private SqlSessionFactory sqlSessionFactory;
 
  /**
   * redis服务bean
   * @return
   */
  @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
  @Lazy(value = false)
  @Bean  // (initMethod = "init", destroyMethod = "destroy")
  public RedisService redisService() {
    String redisUrl = redisHost;
    if (!StringUtils.isEmpty(redisPort))
      redisUrl = redisHost + ":" + redisPort;
    int timeout = redisTimeoutMs;
    return new RedisService("true", redisUrl, timeout, redisPassword);
  }

//  @Bean
//  public SqlSessionTemplate sqlSessionTemplate() throws Exception {
//    SqlSessionTemplate result = new SqlSessionTemplate(sqlSessionFactory);
//    return result;
//  }

  @Bean
  public IBaseDAO baseDAO() throws Exception {
    BaseDAOImpl result = new BaseDAOImpl();
    //result.setSqlSessionTemplate(sqlSessionTemplate());
    result.setSqlSessionTemplate(sqlSessionTemplate);
    return result;
  }

  @Bean
  public PlatformTransactionManager annotationDrivenTransactionManager() {
      return new DataSourceTransactionManager(dataSource);
  }
  
  
}
