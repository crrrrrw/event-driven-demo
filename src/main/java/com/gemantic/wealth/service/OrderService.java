package com.gemantic.wealth.service;

import com.gemantic.wealth.model.Order;
import com.gemantic.wealth.spring.CreateOrderEvent;
import com.gemantic.wealth.spring.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private EventPublisher createOrderEventPublisher;

    /**
     * 插入订单表操作
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(Order order) {
        log.info("[订单入库] start");

        createOrderEventPublisher.publishCreateOrderEvent(new CreateOrderEvent(order));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("[订单入库] end");
    }

}
