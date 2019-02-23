package com.gemantic.wealth.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DuanXinNoticeListener implements ApplicationListener<CreateOrderEvent> {
    @Override
    public void onApplicationEvent(CreateOrderEvent event) {
        log.info("实现接口方式：收到[创建订单]事件。[短信通知]：亲爱的{}，您的订单[{}]已被创建", event.getOrder().getUserName(), event.getOrder().getOrderName());
    }
}
