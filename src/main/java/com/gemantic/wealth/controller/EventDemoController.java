package com.gemantic.wealth.controller;

import com.gemantic.wealth.eventbus.EmptyEvent;
import com.gemantic.wealth.eventbus.EventBusManager;
import com.gemantic.wealth.model.Order;
import com.gemantic.wealth.service.OrderService;
import com.gemantic.wealth.spring.CreateOrderEvent;
import com.gemantic.wealth.spring.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventDemoController {

    @Autowired
    private EventPublisher createOrderEventPublisher;
    @Autowired
    private OrderService orderService;

    @PostMapping("/spring/createOrder")
    public String createOrderBySpring(Order order) {
        createOrderEventPublisher.publishCreateOrderEvent(new CreateOrderEvent(order));
        return "ok";
    }

    @PostMapping("/spring/createOrder_transaction")
    public String createOrder_transactionBySpring(Order order) {
        orderService.insert(order);
        return "ok";
    }

    @PostMapping("/eventbus/createOrder")
    public String createOrderByEventBus(Order order) {
        EventBusManager.CREATE_ORDER.post(new com.gemantic.wealth.eventbus.CreateOrderEvent(order));
        return "ok";
    }

    @PostMapping("/eventbus/empty")
    public String emptyEventByEventBus() {
        EventBusManager.USER_REGISTER.post(new EmptyEvent());
        return "ok";
    }

    @PostMapping("/spring/empty")
    public String emptyEventBySpring() {
        createOrderEventPublisher.publishEmptyEvent(new com.gemantic.wealth.spring.EmptyEvent(this));
        return "ok";
    }

}
