package com.gemantic.wealth.model;

import lombok.Data;

@Data
public class Order {
    private Long id;
    private String orderName;
    private Integer status;
    private String userName;
}
