package com.gemantic.wealth.eventbus;

import com.google.common.eventbus.EventBus;

import javax.annotation.PostConstruct;

/**
 * 抽象的监听器
 */
public abstract class AbstractBusListener {

    /**
     * 统一在spring启动时注册事件
     */
    @PostConstruct
    public void init() {
        registerEventBus();
    }

    private void registerEventBus() {
        getEventBus().register(this);
    }

    protected abstract EventBus getEventBus();
}
