package com.mechempire.client;

import com.mechempire.client.factory.SceneFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    private Parent parent;

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

        // init fxml loader
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(ctx::getBean);
        parent = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneFactory.initStage(parent, primaryStage);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ctx.close();
    }
}