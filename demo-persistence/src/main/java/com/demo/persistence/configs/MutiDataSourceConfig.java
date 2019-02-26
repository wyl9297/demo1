package com.demo.persistence.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
/**
* acl 多数据源配置
* */
@Configuration
public class MutiDataSourceConfig  {

    @Bean(name = "aclDataSource")
    @Qualifier("aclDataSource")
    @ConfigurationProperties(prefix="spring.datasource.acl")
    @Primary
    public DataSource aclDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "aclJdbcTemplate")
    public JdbcTemplate aclJdbcTemplate(@Qualifier("aclDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


     /**
      * 用作配置新数据源参考 start
      */
    @Bean(name = "uniregDataSource")
    @Qualifier("uniregDataSource")
    @ConfigurationProperties(prefix="spring.datasource.unireg")
    public DataSource uniregDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "uniregJdbcTemplate")
    public JdbcTemplate uniregJdbcTemplate(@Autowired @Qualifier("uniregDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    /**
     * 用作配置新数据源参考 end
     */

}