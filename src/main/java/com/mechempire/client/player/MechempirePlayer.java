package com.mechempire.client.player;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午5:02
 */
@Slf4j
@Component
public class MechempirePlayer extends Application {

//    @Resource
//    private MechEmpireClient mechEmpireClient;

//    @Resource
//    private MainController mainController;

    private Parent parent;

    @Override
    public void init() throws Exception {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
//        fxmlLoader.setControllerFactory();
//        parent = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Media media = new Media(getClass().getResource("/4709.wav").toExternalForm());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.setAutoPlay(true);
//        mediaPlayer.setStartTime(Duration.seconds(10));
//        MainController mainController = new MainController();
//        mainController.show(primaryStage);

//        Parent root = FXMLLoader.load(getClass().getResource(FXML_FILE));
//        SceneFactory.initStage(parent, primaryStage);
//        primaryStage.show();
    }

    public static void main(String[] args) {
//        launch(args);
    }
}