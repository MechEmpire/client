package com.mechempire.client.controller;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.service.impl.GameMapServiceImpl;
import com.mechempire.sdk.core.game.GameMapComponent;
import com.mechempire.sdk.runtime.GameMap;
import com.mechempire.sdk.runtime.GameMapComponentFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mapeditor.core.*;
import org.mapeditor.io.TMXMapReader;

import java.util.List;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:27
 */
public class GamePlayerController extends AbstractController {

    private static final String FXML_FILE = "/fxml/game_player.fxml";

    @FXML
    private Pane mapContainer;

    @Override
    public void show(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_FILE));
            fxmlLoader.setController(this);
            Scene newScene = new Scene(fxmlLoader.load(), UIConfig.WINDOW_WIDTH, UIConfig.WINDOW_HEIGHT);
            stage.setTitle(UIConfig.WINDOW_TITLE);
            stage.setScene(newScene);
            stage.show();

            // map reader
            TMXMapReader mapReader = new TMXMapReader();
            Map originMap = null;
            originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
            MapLayer layer = null;

            GameMapService gameMapService = new GameMapServiceImpl();
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