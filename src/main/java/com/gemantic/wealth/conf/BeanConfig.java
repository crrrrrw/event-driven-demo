package com.gemantic.wealth.conf;

import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    // @EnableTransactionManagement 已自动配置
    /*@Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }*/

}
