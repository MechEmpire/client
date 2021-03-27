package com.mechempire.client.view;

import com.mechempire.client.constant.UIConstant;
import com.mechempire.client.controller.GamePlayerController;
import com.mechempire.client.manager.ResourceManager;
import com.mechempire.client.manager.UIManager;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.view
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:01
 */
@Lazy
@Slf4j
@Component
public class HomeView extends AbstractView {

    @Resource
    private UIManager uiManager;

    @Resource
    private ResourceManager resourceManager;

    @Resource
    private GamePlayerController gamePlayerController;

    public void render() {
        root = new Pane();
        Image backgroundImage = resourceManager.getImage("background");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(uiManager.getWindowWidth());
        backgroundImageView.setFitHeight(uiManager.getWindowHeight());

        Image image = resourceManager.getImage("logo");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(uiManager.getMainLogoFitWidth());
        imageView.setFitHeight(uiManager.getMainLogoFitHeight());
        imageView.setX(uiManager.getMainLogoX());
        imageView.setY(uiManager.getMainLogoY());

        Button button = new Button("立即对战");
        button.setStyle(UIConstant.START_BTN_STYLE);
        button.setTextAlignment(TextAlignment.CENTER);
        button.setTextFill(Paint.valueOf("#f1ce76"));
        button.setAlignment(Pos.CENTER);
        button.setCancelButton(true);
        button.setPrefHeight(uiManager.getStartBtnPrefHeight());
        button.setPrefWidth(uiManager.getStartBtnPrefWidth());
        button.setLayoutX(uiManager.getStartBtnX());
        button.setLayoutY(uiManager.getStartBtnY());
        button.setMnemonicParsing(false);
        button.setFont(new Font(uiManager.getStartBtnFontSize()));
        button.setOnAction(actionEvent -> {
            Scene scene = ((Button) actionEvent.getSource()).getScene();
            Stage stage = (Stage) scene.getWindow();

            // todo 这里后续可以改成观察者模式
            gamePlayerController.show(stage);
        });

        root.getChildren().addAll(backgroundImageView, imageView, button);
        makeScaleTransition(backgroundImageView, 10000, 0.25, 0.25);

        uiManager.initCommonStage(stage);
        stage.setScene(new Scene(root, uiManager.getWindowWidth(), uiManager.getWindowHeight()));
        stage.show();
    }
}