package com.gemantic.wealth.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

@Slf4j
@Configuration
public class BeanConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new AbstractPlatformTransactionManager() {
            @Override
            protected Object doGetTransaction() throws TransactionException {
                return new Object();
            }

            @Override
            protected void doBegin(Object o, TransactionDefinition transactionDefinition) throws TransactionException {
                log.info("[transaction] begin...");
            }

            @Override
            protected void doCommit(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
                log.info("[transaction] commit...");
            }

            @Override
            protected void doRollback(DefaultTransactionStatus defaultTransactionStatus) throws TransactionException {
                log.info("[transaction] rollback...");
            }
        };
    }

}
