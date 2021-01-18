package com.mechempire.client.service.impl;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.util.CoordinateUtil;
import com.mechempire.client.util.ImageUtil;
import com.mechempire.sdk.core.factory.GameMapComponentFactoryProducer;
import com.mechempire.sdk.core.game.AbstractGameMapComponent;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.mapeditor.core.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * package: com.mechempire.client.service.impl
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午3:15
 */
@Service("GameMapService")
public class GameMapServiceImpl implements GameMapService {

    @Override
    public void initGameMapBackground(MapLayer layer, Pane mapContainer) {
        ImageData backgroundImageData = ((ImageLayer) layer).getImage();
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(
                        backgroundImageData.getSource(),
                        CoordinateUtil.coordinateXConvert(backgroundImageData.getWidth()),
                        CoordinateUtil.coordinateYConvert(backgroundImageData.getHeight()),
                        false,
                        true
                ),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        mapContainer.setPrefWidth(UIConfig.WINDOW_WIDTH);
        mapContainer.setPrefHeight(UIConfig.WINDOW_HEIGHT);
        mapContainer.setBackground(new Background(backgroundImage));
    }

    @Override
    public void initGameMapTile(MapLayer layer, Pane mapContainer) {
        ImageData imageData = ((ImageLayer) layer).getImage();
        Image image = new Image(imageData.getSource());
        ImageView view = new ImageView(image);
        view.setX(CoordinateUtil.coordinateXConvert(layer.getOffsetX()));
        view.setY(CoordinateUtil.coordinateYConvert(layer.getOffsetY()));
        view.setFitWidth(CoordinateUtil.coordinateXConvert(imageData.getWidth()));
        view.setFitHeight(CoordinateUtil.coordinateYConvert(imageData.getHeight()));
        if (null != layer.getOpacity()) {
            view.setOpacity(layer.getOpacity());
        }
        mapContainer.setPrefHeight(CoordinateUtil.coordinateYConvert(image.getHeight()));
        mapContainer.setPrefWidth(CoordinateUtil.coordinateXConvert(image.getWidth()));
        mapContainer.getChildren().add(view);
    }

    @Override
    public void initTileLayer(Map originMap, MapLayer layer, Pane mapContainer) {
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
                imageView.setTranslateX(x * CoordinateUtil.coordinateXConvert(tileWidth));
                imageView.setTranslateY(y * CoordinateUtil.coordinateYConvert(tileHeight));
                mapContainer.getChildren().add(imageView);
            }
        }
    }

    @Override
    public GameMap initGameMapObject(Map originMap) {
        GameMap gameMap = new GameMap();
        gameMap.setWidth(originMap.getWidth() * originMap.getTileWidth());
        gameMap.setLength(originMap.getHeight() * originMap.getTileHeight());
        gameMap.setGridWidth(originMap.getTileWidth());
        gameMap.setGridLength(originMap.getTileHeight());
        gameMap.setName(originMap.getFilename());
        return gameMap;
    }

    @Override
    public void initGameMapComponent(MapLayer layer, GameMap gameMap) {
        List<MapObject> objectList = ((ObjectGroup) layer).getObjects();
        for (MapObject mapObject : objectList) {
            AbstractGameMapComponent gameMapComponent =
                    GameMapComponentFactoryProducer.getComponent(layer.getName(), (short) 1);

            if (null == gameMapComponent) {
                continue;
            }

            gameMapComponent.setName(mapObject.getName());
            gameMapComponent.setAffinity(Short.parseShort(mapObject.getProperties().getProperties().get(0).getValue()));
            gameMapComponent.setId(mapObject.getId());
            gameMapComponent.setLength(mapObject.getHeight());
            gameMapComponent.setWidth(mapObject.getWidth());
//            gameMapComponent.setPositionY(mapObject.getY());
//            gameMapComponent.setPositionX(mapObject.getX());
            gameMapComponent.setType(layer.getName());

            gameMap.addMapComponent(gameMapComponent);
        }
    }
}