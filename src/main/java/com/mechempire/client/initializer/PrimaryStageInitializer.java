package com.mechempire.client.initializer;

import com.mechempire.client.Render;
import com.mechempire.client.event.StageReadyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.initializer
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-18 09:40
 */
@Slf4j
@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    @Resource
    private Render render;

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        render.setStage(stageReadyEvent.stage);
        render.home();
    }
}
