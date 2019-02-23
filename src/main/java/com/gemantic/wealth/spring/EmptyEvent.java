package com.gemantic.wealth.spring;

import org.springframework.context.ApplicationEvent;

public class EmptyEvent extends ApplicationEvent {
    public EmptyEvent(Object source) {
        super(source);
    }
}
