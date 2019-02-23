package com.gemantic.wealth.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishCreateOrderEvent(CreateOrderEvent createOrderEvent) {
        log.info("发布了一个[订单创建]事件：{}", createOrderEvent);
        applicationEventPublisher.publishEvent(createOrderEvent);
    }

    public void publishEmptyEvent(EmptyEvent emptyEvent) {
        log.info("发布了一个[空]事件：{}", emptyEvent);
        applicationEventPublisher.publishEvent(emptyEvent);
    }
}
