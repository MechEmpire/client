package com.mechempire.client.player;

import com.mechempire.client.controller.MainController;
import com.mechempire.client.network.MechEmpireClient;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午5:02
 */
@Slf4j
@Component
public class MechempirePlayer extends Application {

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Media media = new Media(getClass().getResource("/4709.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setAutoPlay(true);
//        mediaPlayer.setStartTime(Duration.seconds(10));
        MainController mainController = new MainController();
        mainController.show(primaryStage);
    }
}