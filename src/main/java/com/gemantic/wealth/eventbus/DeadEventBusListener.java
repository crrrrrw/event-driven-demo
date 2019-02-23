package com.gemantic.wealth.eventbus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadEventBusListener extends AbstractBusListener {

    @AllowConcurrentEvents
    @Subscribe
    public void onListenDeadEvent(DeadEvent event) {
        log.info("[eventbus] dead event: {}", event);
    }

    @Override
    protected EventBus getEventBus() {
        return EventBusManager.USER_REGISTER;
    }
}
