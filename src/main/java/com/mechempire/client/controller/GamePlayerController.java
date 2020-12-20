package com.mechempire.client.controller;

import com.mechempire.client.factory.SceneFactory;
import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.runtime.GameMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.ObjectGroup;
import org.mapeditor.core.TileLayer;
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
public class GamePlayerController extends AbstractController {

    private static final String FXML_FILE = "/fxml/game_player.fxml";

    @FXML
    private Pane mapContainer;

    @Resource
    private GameMapService gameMapService;

    @Override
    public void show(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_FILE));
            fxmlLoader.setController(this);
            SceneFactory.initStage(fxmlLoader.load(), stage);
            stage.show();

            // map reader
            TMXMapReader mapReader = new TMXMapReader();
            Map originMap = null;
            originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
            MapLayer layer = null;

            GameMap gameMap = gameMapService.initGameMapObject(originMap);

            for (int i = 0; i < originMap.getLayerCount(); i++) {
                layer = originMap.getLayer(i);
                if (layer.getName().equals("background")) {
                    gameMapService.initGameMapBackground(layer, mapContainer);
                } else if (layer.getName().equals("logo")) {
                    gameMapService.initGameMapLogo(layer, mapContainer);
                } else if (layer instanceof TileLayer) {
                    gameMapService.initTileLayer(originMap, layer, mapContainer);
                } else if (layer instanceof ObjectGroup) {
                    gameMapService.initGameMapComponent(layer, gameMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}