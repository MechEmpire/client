package com.mechempire.client.event;

import org.springframework.context.ApplicationEvent;

/**
 * package: com.mechempire.client.event
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-19 22:27
 */
public class ClientReadyEvent extends ApplicationEvent {

    public ClientReadyEvent(Object source) {
        super(source);
    }
}
