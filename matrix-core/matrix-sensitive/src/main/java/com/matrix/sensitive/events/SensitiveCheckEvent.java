package com.matrix.sensitive.events;


import org.springframework.context.ApplicationEvent;

/**
 * @author ZhaoWeiLong
 * @since 2023/4/20
 **/
public class SensitiveCheckEvent extends ApplicationEvent {

    public SensitiveCheckEvent(Object source) {
        super(source);
    }
}
