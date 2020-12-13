package com.mechempire.client.controller;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.service.impl.GameMapServiceImpl;
import com.mechempire.client.util.ImageUtil;
import com.mechempire.sdk.core.game.GameMapComponent;
import com.mechempire.sdk.runtime.GameMap;
import com.mechempire.sdk.runtime.GameMapComponentFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.mapeditor.core.*;
import org.mapeditor.io.TMXMapReader;

import java.util.HashMap;
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

    private TMXMapReader mapReader = new TMXMapReader();
    private Map originMap = null;

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
            originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
            MapLayer layer = null;

            GameMapService gameMapService = new GameMapServiceImpl();
            GameMap gameMap = gameMapService.initGameMap(originMap);

            for (int i = 0; i < originMap.getLayerCount(); i++) {
                layer = originMap.getLayer(i);
                if (layer.getName().equals("background")) {
                    mapContainer.setPrefWidth(UIConfig.WINDOW_WIDTH);
                    mapContainer.setPrefHeight(UIConfig.WINDOW_HEIGHT);
                    ImageData backgroundImageData = ((ImageLayer) layer).getImage();
                    BackgroundImage backgroundImage = new BackgroundImage(
                            new Image(backgroundImageData.getSource(), backgroundImageData.getWidth(), backgroundImageData.getHeight(), false, true),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            BackgroundSize.DEFAULT
                    );
                    mapContainer.setBackground(new Background(backgroundImage));
                } else if (layer.getName().equals("logo")) {
                    ImageData logoImageData = ((ImageLayer) layer).getImage();
                    Image logoImage = new Image(logoImageData.getSource());
                    ImageView logoView = new ImageView(logoImage);
                    logoView.setX(layer.getOffsetX());
                    logoView.setY(layer.getOffsetY());
                    logoView.setFitWidth(logoImage.getWidth());
                    logoView.setFitHeight(logoImageData.getHeight());
                    logoView.setOpacity(layer.getOpacity());
                    mapContainer.setPrefHeight(logoImage.getHeight());
                    mapContainer.setPrefWidth(logoImage.getWidth());
                    mapContainer.getChildren().add(logoView);
                } else if (layer instanceof TileLayer) {
                    int width = originMap.getWidth();
                    int height = originMap.getHeight();
                    int tileWidth = originMap.getTileWidth();
                    int tileHeight = originMap.getTileHeight();

                    Tile tile = null;
                    int tid;

                    HashMap<Integer, Image> tileHash = new HashMap<>(8);
                    Image tileImage = null;
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            tile = ((TileLayer) layer).getTileAt(x, y);
                            if (null == tile) {
                                continue;
                            }
                            tid = tile.getId();
                            if (tileHash.containsKey(tid)) {
                                tileImage = tileHash.get(tid);
                            } else {
                                tileImage = ImageUtil.awtImageToJavaxImage(tile.getImage());
                                tileHash.put(tid, tileImage);
                            }

                            ImageView imageView = new ImageView(tileImage);
                            imageView.setTranslateX(x * tileWidth);
                            imageView.setTranslateY(y * tileHeight);
                            mapContainer.getChildren().add(imageView);
                        }
                    }
                } else if (layer instanceof ObjectGroup) {
                    List<MapObject> objectList = ((ObjectGroup) layer).getObjects();
                    for (MapObject mapObject : objectList) {
                        GameMapComponent gameMapComponent = GameMapComponentFactory.getGameMapComponent(layer.getName());

                        if (null == gameMapComponent) {
                            continue;
                        }

                        gameMapComponent.setName(mapObject.getName())
                                .setAffinity(Short.parseShort(mapObject.getProperties().getProperties().get(0).getValue()))
                                .setId(mapObject.getId())
                                .setHeight(mapObject.getHeight())
                                .setWidth(mapObject.getWidth())
                                .setPositionY(mapObject.getY())
                                .setPositionX(mapObject.getX())
                                .setType(layer.getName());

                        gameMap.addMapComponent(gameMapComponent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}