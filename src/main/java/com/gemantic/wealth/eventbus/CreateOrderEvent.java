package com.gemantic.wealth.eventbus;


import com.gemantic.wealth.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderEvent {
    private Order order;
}
