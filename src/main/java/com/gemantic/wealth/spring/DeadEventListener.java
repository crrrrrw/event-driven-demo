package com.gemantic.wealth.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeadEventListener {

    @EventListener
    public void testDeadEvent(PayloadApplicationEvent event) {
        log.info("[spring] dead event: {}", event);
    }

}
