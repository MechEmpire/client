package com.mechempire.client.initializer;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.controller.GamePlayerController;
import com.mechempire.client.event.StageReadyEvent;
import com.mechempire.client.factory.SceneFactory;
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
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.initializer
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-18 09:40
 */
@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    @Resource
    GamePlayerController gamePlayerController;

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        Stage primaryStage = stageReadyEvent.stage;
        SceneFactory.initCommonStage(primaryStage);

        Pane root = new Pane();
        root.setStyle(UIConfig.MAIN_SCENE_BACKGROUND);
        Image image = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(UIConfig.MAIN_LOGO_FIT_WIDTH);
        imageView.setFitHeight(UIConfig.MAIN_LOGO_FIT_HEIGHT);
        imageView.setX(UIConfig.MAIN_LOGO_X);
        imageView.setY(UIConfig.MAIN_LOGO_Y);

        Button button = new Button("立即对战");
        button.setStyle(UIConfig.START_BTN_STYLE);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setTextFill(Paint.valueOf("#f1ce76"));
        button.setAlignment(Pos.CENTER);
        button.setCancelButton(true);
        button.setPrefHeight(UIConfig.START_BTN_PREF_HEIGHT);
        button.setPrefWidth(UIConfig.START_BTN_PREF_WIGHT);
        button.setLayoutX(UIConfig.START_BTN_X);
        button.setLayoutY(UIConfig.START_BTN_Y);
        button.setMnemonicParsing(false);
        button.setFont(new Font(UIConfig.START_BTN_FONT_SIZE));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Scene scene = ((Button) actionEvent.getSource()).getScene();
                Window window = scene.getWindow();
                Stage stage = (Stage) window;
                gamePlayerController.show(stage);
            }
        });

        root.getChildren().addAll(imageView, button);
        primaryStage.setScene(new Scene(root, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));
        primaryStage.show();
    }
}
