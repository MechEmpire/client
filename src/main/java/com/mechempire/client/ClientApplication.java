package com.mechempire.client;

import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.player.MechempirePlayer;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/18 上午11:59
 */
@Slf4j
public class ClientApplication {

    /**
     * 入口函数
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("com.mechempire");
        ctx.refresh();

        // run network client
        Thread empireClientThread = new Thread(() -> {
            try {
                MechEmpireClient mechEmpireClient = ctx.getBean(MechEmpireClient.class);
                mechEmpireClient.run();
            } catch (Exception e) {
                log.error("run expire client error: {}", e.getMessage(), e);
            }
        });
        empireClientThread.start();

        // run player
        Thread mechempirePlayerThread = new Thread(() -> {
            Application.launch(MechempirePlayer.class, args);
        });
        mechempirePlayerThread.start();
        ctx.close();
    }
}