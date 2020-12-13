package com.mechempire.client.controller;

import com.mechempire.client.config.UIConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> gengrui@qury.org
 * @date 2020/12/12 下午5:01
 */
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
            stage.setTitle(UIConfig.WINDOW_TITLE);
            stage.setScene(new Scene(root, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}