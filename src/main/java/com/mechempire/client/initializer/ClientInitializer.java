package com.mechempire.client.initializer;

import com.mechempire.client.event.ClientReadyEvent;
import com.mechempire.client.network.MechEmpireClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.initializer
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-19 22:29
 */
@Slf4j
@Component
public class ClientInitializer implements ApplicationListener<ClientReadyEvent> {

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Override
    public void onApplicationEvent(ClientReadyEvent clientReadyEvent) {
        try {
            mechEmpireClient.run();
        } catch (Exception e) {
            log.error("client start error: {}", e.getMessage(), e);
        }
    }
}
