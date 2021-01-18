package com.mechempire.client.factory;

import com.mechempire.client.config.UIConfig;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * package: com.mechempire.client.factory
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/20 下午12:05
 */
public class SceneFactory {

    /**
     * 初始化 stage
     *
     * @param parent root
     * @param stage  stage
     */
    public static void initStage(Parent parent, Stage stage) {
        Scene scene = new Scene(parent, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT);
        stage.setTitle(UIConfig.WINDOW_TITLE);
        stage.setMinWidth(UIConfig.WINDOW_WIDTH);
        stage.setMinHeight(UIConfig.WINDOW_HEIGHT);
        stage.setMaxWidth(UIConfig.WINDOW_WIDTH);
        stage.setMaxHeight(UIConfig.WINDOW_HEIGHT);
        stage.setScene(scene);
    }


    /**
     * 初始化通用 stage
     *
     * @param stage stage
     */
    public static void initCommonStage(Stage stage) {
        stage.setTitle(UIConfig.WINDOW_TITLE);
        stage.setMinWidth(UIConfig.WINDOW_WIDTH);
        stage.setMinHeight(UIConfig.WINDOW_HEIGHT);
        stage.setMaxWidth(UIConfig.WINDOW_WIDTH);
        stage.setMaxHeight(UIConfig.WINDOW_HEIGHT);
    }
}