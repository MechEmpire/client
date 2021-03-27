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
        DestroyerVehicle destroyerVehicle = new DestroyerVehicle();
        destroyerVehicle.setId(111);
        Rectangle rectangle = new Rectangle(destroyerVehicle.getStartX(), destroyerVehicle.getStartY(),
                destroyerVehicle.getWidth(), destroyerVehicle.getLength());
        rectangle.setFill(Paint.valueOf("#ffffff"));
        destroyerVehicle.setShape(rectangle);
        gameMap.addMapComponent(destroyerVehicle);

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