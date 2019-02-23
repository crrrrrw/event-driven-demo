package com.gemantic.wealth.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * 事件管理器：统一管理所有事件
 */
public class EventBusManager {
    /**
     * 用户注册事件
     */
    public final static EventBus USER_REGISTER = new EventBus();

    /**
     * 创建订单
     */
    //public final static EventBus CREATE_ORDER = new AsyncEventBus(Executors.newFixedThreadPool(5));
    public final static EventBus CREATE_ORDER = new EventBus();
}
