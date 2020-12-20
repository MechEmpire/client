package com.mechempire.client.controller;

import com.mechempire.client.factory.SceneFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午5:01
 */
@Slf4j
public class MainController extends AbstractController {

    private static final String FXML_FILE = "/fxml/main.fxml";

    @FXML
    Button beginBtn;

    @FXML
    public void onBeginBtnClick() {
        try {
            Scene scene = beginBtn.getScene();
            Window window = scene.getWindow();
            Stage stage = (Stage) window;

            GamePlayerController gamePlayerController = new GamePlayerController();
            gamePlayerController.show(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示当前场景
     *
     * @param stage
     */
    @Override
    public void show(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(FXML_FILE));
            SceneFactory.initStage(root, stage);
            stage.show();
        } catch (Exception e) {
            log.error("show main state error: {}", e.getMessage(), e);
        }
    }
}