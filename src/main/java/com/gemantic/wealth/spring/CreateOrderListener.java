package com.gemantic.wealth.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 注解式的spring event
 */
@Slf4j
@Component
public class CreateOrderListener {

    @Async
    @EventListener(condition = "#createOrderEvent.order.status == 1")
    public void processCreateOrderEvent(CreateOrderEvent createOrderEvent) {
        log.info("注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }

    @Async
    @EventListener(condition = "#createOrderEvent.order.status == 2")
    public void processCreateOrderEvent2(CreateOrderEvent createOrderEvent) {
        log.info("注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }

    @EventListener(condition = "#createOrderEvent.order.status == 3")
    public void processCreateOrderEvent3(CreateOrderEvent createOrderEvent) {
        log.info("[EventListener] 注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }

    @TransactionalEventListener(condition = "#createOrderEvent.order.status == 3", phase = TransactionPhase.AFTER_COMMIT)
    public void processCreateOrderEvent4(CreateOrderEvent createOrderEvent) {
        log.info("[TransactionalEventListener] 注解式 spring event 收到消息，订单名称:{} ; 订单状态为：{} ; 开始处理相应的事件。", createOrderEvent.getOrder().getOrderName(), createOrderEvent.getOrder().getStatus());
    }

    /**
     * 测试父类的事件的继承
     * 此事件为 Object
     *
     * @param object
     */
    @EventListener
    public void testParentEvent(Object object) {
        log.info("[spring] 测试父类的事件的继承: {}", object);
    }

    /**
     * 测试事件异常
     *
     * @param event
     */
    @Order(1)
    @EventListener
    public void testExceptionEvent(CreateOrderEvent event) {
        log.info("[spring] exception event: {}", event);
        throw new RuntimeException("发生异常了");
    }
}
