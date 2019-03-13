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
     * 配置审批数据源 start
     * @return
     */
    @Bean(name = "approveDataSource")
    @Qualifier("approveDataSource")
    @ConfigurationProperties(prefix="spring.datasource.approve")
    public DataSource approveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "approveJdbcTemplate")
    public JdbcTemplate approveJdbcTemplate(@Autowired @Qualifier("approveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     *  配置审批数据源  end
     */


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

    /**
     * 用作配置新数据源参考 start
     */
    @Bean(name = "purchaseDataSource")
    @Qualifier("purchaseDataSource")
    @ConfigurationProperties(prefix="spring.datasource.purchase")
    public DataSource purchaseDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "purchaseJdbcTemplate")
    public JdbcTemplate testJdbcTemplate(@Autowired @Qualifier("purchaseDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    /**
     * 用作配置新数据源参考 end
     */
    /**
     * 用作配置新数据源参考 start
     */
/*    @Bean(name = "yuecaiProDataSource")
    @Qualifier("yuecaiProDataSource")
    @ConfigurationProperties(prefix="spring.datasource.yuecai_pro")
    public DataSource yuecaiProDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "yuecaiProJdbcTemplate")
    public JdbcTemplate yuecaiProJdbcTemplate(@Autowired @Qualifier("yuecaiProDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }*/
    /**
     * 用作配置新数据源参考 end
     */

}