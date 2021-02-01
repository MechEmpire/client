package com.mechempire.client;

import com.mechempire.client.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/18 上午11:59
 */
@Slf4j
public class ClientApplication extends Application {

    /**
     * Spring 上下文对象
     */
    private final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

    /**
     * 入口函数
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        ctx.scan("com.mechempire");
        ctx.refresh();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ctx.getBean(Render.class).setStage(primaryStage);
        ctx.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() throws Exception {
        ctx.close();
        Platform.exit();
    }
}