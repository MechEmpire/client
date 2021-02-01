package com.mechempire.client.view;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.constant.UIConstant;
import com.mechempire.client.manager.ResourceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * package: com.mechempire.client.view
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:01
 */
@Slf4j
@Component
public class HomeView extends AbstractView {

//    @Resource
//    private ResourceManager resourceManager;

    public HomeView(UIConfig uiConfig, ResourceManager resourceManager) {
        root = new Pane();

//        Image backgroundImage = new Image(getClass().getResourceAsStream("/image/background.jpg"));
        Image backgroundImage = resourceManager.getImage("background.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(uiConfig.getWindowWidth());
        backgroundImageView.setFitHeight(uiConfig.getWindowHeight());

        Image image = new Image(getClass().getResourceAsStream("/image/logo.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(uiConfig.getMainLogoFitWidth());
        imageView.setFitHeight(uiConfig.getMainLogoFitHeight());
        imageView.setX(uiConfig.getMainLogoX());
        imageView.setY(uiConfig.getMainLogoY());

        Button button = new Button("立即对战");
        button.setStyle(UIConstant.START_BTN_STYLE);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setTextFill(Paint.valueOf("#f1ce76"));
        button.setAlignment(Pos.CENTER);
        button.setCancelButton(true);
        button.setPrefHeight(uiConfig.getStartBtnPrefHeight());
        button.setPrefWidth(uiConfig.getStartBtnPrefWidth());
        button.setLayoutX(uiConfig.getStartBtnX());
        button.setLayoutY(uiConfig.getStartBtnY());
        button.setMnemonicParsing(false);
        button.setFont(new Font(uiConfig.getStartBtnFontSize()));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Scene scene = ((Button) actionEvent.getSource()).getScene();
                Window window = scene.getWindow();
                Stage stage = (Stage) window;
//                gamePlayerController.show(stage);
//
//                // 连接服务器, 同步数据
//                new Thread(() -> {
//                    try {
//                        mechEmpireClient.run();
//                    } catch (Exception e) {
//                        log.error("client run error: {}", e.getMessage(), e);
//                    }
//                }).start();
            }
        });

        root.getChildren().addAll(backgroundImageView, imageView, button);
        makeScaleTransition(backgroundImageView, 10000, 0.25, 0.25);
    }
}