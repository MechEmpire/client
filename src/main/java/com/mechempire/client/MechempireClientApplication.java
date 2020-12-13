package com.mechempire.client;

import com.mechempire.client.controller.MainController;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> gengrui@qury.org
 * @date 2020/12/12 下午5:02
 */
public class MechempireClientApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
//            Media media = new Media(getClass().getResource("/4709.wav").toExternalForm());
//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setAutoPlay(true);
//            mediaPlayer.setStartTime(Duration.seconds(10));

            MainController mainController = new MainController();
            mainController.show(primaryStage);

//            primaryStage.c
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}