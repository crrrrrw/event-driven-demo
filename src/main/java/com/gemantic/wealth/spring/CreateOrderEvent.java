package com.gemantic.wealth.spring;


import com.gemantic.wealth.model.Order;
import org.springframework.context.ApplicationEvent;

public class CreateOrderEvent extends ApplicationEvent {

    public CreateOrderEvent(Object source) {
        super(source);
    }

    public Order getOrder() {
        return (Order) this.source;
    }
}
