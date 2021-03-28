package com.mechempire.client.controller;

import com.mechempire.client.manager.UIManager;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.view.GamePlayerView;
import com.mechempire.sdk.core.component.DestroyerVehicle;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:27
 */
@Lazy
@Slf4j
@Controller
public class GamePlayerController extends AbstractController {

    @Resource
    private GameMapService gameMapService;

    @Resource
    private UIManager uiManager;

    @Resource
    private GameMap gameMap;

    @Resource
    private GamePlayerView gamePlayerView;

    @Override
    public void show(Stage stage) {
        // 增加一个毁灭者载具
        DestroyerVehicle destroyerVehicleRed = new DestroyerVehicle();
        destroyerVehicleRed.setId(0);
        Rectangle rectangleRed = new Rectangle(destroyerVehicleRed.getStartX(), destroyerVehicleRed.getStartY(),
                destroyerVehicleRed.getWidth(), destroyerVehicleRed.getLength());
        rectangleRed.setFill(Paint.valueOf("#ff0000"));
        destroyerVehicleRed.setShape(rectangleRed);
        gameMap.addMapComponent(destroyerVehicleRed);

        DestroyerVehicle destroyerVehicleBlue = new DestroyerVehicle();
        destroyerVehicleBlue.setId(4);
        Rectangle rectangleBlue = new Rectangle(destroyerVehicleBlue.getStartX(), destroyerVehicleBlue.getStartY(),
                destroyerVehicleBlue.getWidth(), destroyerVehicleBlue.getLength());
        rectangleBlue.setFill(Paint.valueOf("#0000ff"));
        destroyerVehicleBlue.setShape(rectangleBlue);
        gameMap.addMapComponent(destroyerVehicleBlue);

        gamePlayerView.setStage(stage);
        gamePlayerView.render();

//        gameMapService.getGameMap().getComponents();

//        AbstractGameMapComponent sprite = GameMapComponentFactory.createComponent();


//        gameMapService.getGameMap().add

        // 17
        //
//        DefaultObstacle defaultObstacle = (DefaultObstacle) gameMapService.getGameMap().getMapComponent(17);
//        log.info("obstacle: {}", defaultObstacle.getName());


//        log.info("game_map: {}", gameMapService.getGameMap().getComponents());

        // main loop

        // pull data
        // update ui
    }
}