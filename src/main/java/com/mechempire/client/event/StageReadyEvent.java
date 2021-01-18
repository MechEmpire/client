package com.mechempire.client.event;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

/**
 * package: com.mechempire.client.event
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-18 09:37
 */
public class StageReadyEvent extends ApplicationEvent {

    public final Stage stage;

    public StageReadyEvent(Stage source) {
        super(source);
        this.stage = source;
    }
}
