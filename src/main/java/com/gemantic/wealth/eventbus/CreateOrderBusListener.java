package com.gemantic.wealth.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderBusListener extends AbstractBusListener {

    /**
     * 订单创建事件：短信通知
     */
    @AllowConcurrentEvents
    @Subscribe
    public void onCreateOrderEvent2DuanxinNotice(CreateOrderEvent event) {
        log.info("收到[创建订单]事件。[短信通知]：亲爱的{}，您的订单[{}]已被创建", event.getOrder().getUserName(), event.getOrder().getOrderName());
    }

    /**
     * 订单创建事件：微信通知
     */
    @AllowConcurrentEvents
    @Subscribe
    public void onCreateOrderEvent2WeixinNotice(CreateOrderEvent event) {
        log.info("收到[创建订单]事件。[微信通知]：亲爱的{}，您的订单[{}]已被创建", event.getOrder().getUserName(), event.getOrder().getOrderName());
    }

    /**
     * 订单创建事件：微信通知
     */
    @AllowConcurrentEvents
    @Subscribe
    public void testParentEvent(Object event) {
        log.info("[eventbus] 测试父类的事件的继承: {}", event);
    }

    /**
     * 测试事件异常
     * @param event
     */
    @AllowConcurrentEvents
    @Subscribe
    public void testExceptionEvent(CreateOrderEvent event) {
        log.info("[eventbus] exception event: {}", event);
        throw new RuntimeException("发生异常了");
    }

    /**
     * 注册的为订单创建事件
     */
    @AllowConcurrentEvents
    @Override
    protected EventBus getEventBus() {
        return EventBusManager.CREATE_ORDER;
    }
}
