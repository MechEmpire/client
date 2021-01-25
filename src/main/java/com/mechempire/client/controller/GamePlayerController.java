package com.mechempire.client.controller;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.factory.SceneFactory;
import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.mapeditor.core.ImageLayer;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.ObjectGroup;
import org.mapeditor.io.TMXMapReader;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:27
 */
@Component
@Slf4j
public class GamePlayerController extends AbstractController {

    @Resource
    private GameMapService gameMapService;

    @Override
    public void show(Stage stage) {
        try {
            Pane mapContainer = new Pane();
            // map reader
            TMXMapReader mapReader = new TMXMapReader();
            Map originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
            MapLayer layer = null;

            GameMap gameMap = gameMapService.initGameMapObject(originMap);

            for (int i = 0; i < originMap.getLayerCount(); i++) {
                layer = originMap.getLayer(i);

                if (layer instanceof ImageLayer) {
                    if (layer.getName().equals("background")) {
                        gameMapService.initGameMapBackground(layer, mapContainer);
                    } else {
                        gameMapService.initGameMapTile(layer, mapContainer);
                    }
                } else if (layer instanceof ObjectGroup) {
                    gameMapService.initGameMapComponent(layer, gameMap);
                }
            }
            SceneFactory.initCommonStage(stage);

//            HashMap<Integer, AbstractGameMapComponent> components = gameMap.getComponents();
//            components.forEach((k, v) -> {
//                AbstractGameMapComponent component = gameMap.getMapComponent(v.getId());
//
//                if (v.getShape() instanceof Rectangle2D) {
//                    Rectangle rectangle = new Rectangle();
//                    rectangle.setX(component.getStartX());
//                    rectangle.setY(component.getStartY());
//                    rectangle.setWidth(component.getWidth());
//                    rectangle.setHeight(component.getLength());
//                    rectangle.setFill(Paint.valueOf("#ffffff"));
//                    mapContainer.getChildren().add(rectangle);
//                } else if (v.getShape() instanceof Ellipse2D) {
//                    Ellipse ellipse = new Ellipse();
//                    ellipse.setCenterX(component.getStartX() + component.getWidth() / 2.0);
//                    ellipse.setCenterY(component.getStartY() + component.getLength() / 2.0);
//                    ellipse.setRadiusX(component.getWidth() / 2.0);
//                    ellipse.setRadiusY(component.getLength() / 2.0);
//                    ellipse.setFill(Paint.valueOf("#ffffff"));
//                    mapContainer.getChildren().add(ellipse);
//                }
//            });

            stage.setScene(new Scene(mapContainer, UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT));
            stage.show();
        } catch (Exception e) {
            log.error("init map error: {}", e.getMessage(), e);
        }
    }
}