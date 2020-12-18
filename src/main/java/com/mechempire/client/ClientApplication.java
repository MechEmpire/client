package com.mechempire.client;

import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.player.MechempirePlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/18 上午11:59
 */
@Slf4j
public class ClientApplication {

    @Resource
    private static MechEmpireClient mechEmpireClient;

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
                mechEmpireClient.run();
            } catch (InterruptedException e) {
                log.error("run expire client error: {}", e.getMessage(), e);
            }
        });
        empireClientThread.start();

        // run player
        Thread mechempirePlayerThread = new Thread(() -> {
            MechempirePlayer.launch(args);
        });
        mechempirePlayerThread.start();
        ctx.close();
    }
}