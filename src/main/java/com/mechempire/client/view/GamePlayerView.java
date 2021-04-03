package com.mechempire.client.view;

import com.mechempire.client.manager.UIManager;
import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.core.component.DestroyerVehicle;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.view
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-02-01 20:32
 */
@Lazy
@Slf4j
@Component
public class GamePlayerView extends AbstractView {

    @Resource
    private UIManager uiManager;

    @Resource
    private GameMapService gameMapService;

    @Resource
    private GameMap gameMap;

    @Override
    public void render() {
        try {
            root = new Pane();

            // map reader
//            TMXMapReader mapReader = new TMXMapReader();
//            Map originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
//            MapLayer layer;

//            gameMapService.initGameMapObject(originMap);
//            Canvas canvas = new Canvas(gameMap.getWidth(), gameMap.getLength());
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//
//            gc.setStroke(Color.RED);
//            gc.setLineWidth(5);
//            gc.strokeLine(20, 60, 120, 60);
//
//            gc.setStroke(Color.BLUE);
//            gc.setLineWidth(5);
//            gc.strokeLine(1160, 60, 1260, 60);
//            root.getChildren().add(canvas);

            log.info("{}", Thread.currentThread().getName());
            root.setBackground(gameMap.getBackground());
            gameMap.getImageViewList().forEach(imageView -> root.getChildren().add(imageView));

            log.info("game_map_component: {}", gameMap.getComponents());

            DestroyerVehicle destroyerVehicleRed = (DestroyerVehicle) gameMap.getMapComponent(0);
            DestroyerVehicle destroyerVehicleBlue = (DestroyerVehicle) gameMap.getMapComponent(4);
            root.getChildren().add(destroyerVehicleRed.getShape());
//            root.getChildren().add(destroyerVehicleBlue.getShape());

            uiManager.initCommonStage(stage);
            stage.setScene(new Scene(root, uiManager.getWindowWidth(), uiManager.getWindowHeight()));
            stage.show();
        } catch (Exception e) {
            log.error("init map error: {}", e.getMessage(), e);
        }
    }
}
