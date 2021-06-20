package com.mechempire.client.service.impl;

import com.mechempire.client.manager.ResourceManager;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.util.ImageUtil;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.extern.slf4j.Slf4j;
import org.mapeditor.core.*;
import org.mapeditor.io.TMXMapReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * package: com.mechempire.client.service.impl
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午3:15
 */
@Lazy
@Slf4j
@Service
public class GameMapServiceImpl implements GameMapService {

    @Resource
    private GameMap gameMap;

    @Resource
    private UIManager uiManager;

    @Resource
    private ResourceManager resourceManager;

    @Override
    public Map getOriginMap(String mapName) {
        try {
            TMXMapReader mapReader = new TMXMapReader();
            return mapReader.readMap(Objects.requireNonNull(getClass().getResource("/map/" + mapName)).toString());
        } catch (Exception e) {
            log.error("read map filer error: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void initGameMapBackground(MapLayer layer) {
        ImageData backgroundImageData = ((ImageLayer) layer).getImage();
        Image sourceImage = resourceManager.getImage(backgroundImageData.getSource());
        BackgroundImage backgroundImage = new BackgroundImage(
                sourceImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
//        gameMap.setBackground(new Background(backgroundImage));
    }

    @Override
    public void initGameMapTile(MapLayer layer) {
        ImageData imageData = ((ImageLayer) layer).getImage();
        Image image = resourceManager.getImage(imageData.getSource());
        ImageView view = new ImageView(image);
        view.setX(uiManager.coordinateXConvert(layer.getOffsetX()));
        view.setY(uiManager.coordinateYConvert(layer.getOffsetY()));
        view.setFitWidth(uiManager.coordinateXConvert(imageData.getWidth()));
        view.setFitHeight(uiManager.coordinateYConvert(imageData.getHeight()));
        if (Objects.nonNull(layer.getOpacity())) {
            view.setOpacity(layer.getOpacity());
        }
//        gameMap.getImageViewList().add(view);
        // todo 这两句作用暂定
//        mapContainer.setPrefHeight(uiManager.coordinateYConvert(image.getHeight()));
//        mapContainer.setPrefWidth(uiManager.coordinateXConvert(image.getWidth()));
//        mapContainer.getChildren().add(view);
    }

    @Override
    public void initTileLayer(Map originMap, MapLayer layer, Pane mapContainer) {
        int width = originMap.getWidth();
        int height = originMap.getHeight();
        int tileWidth = originMap.getTileWidth();
        int tileHeight = originMap.getTileHeight();

        Tile tile;
        int tid;

        HashMap<Integer, Image> tileHash = new HashMap<>(8);
        Image tileImage;
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
                imageView.setTranslateX(x * uiManager.coordinateXConvert(tileWidth));
                imageView.setTranslateY(y * uiManager.coordinateYConvert(tileHeight));
                mapContainer.getChildren().add(imageView);
            }
        }
    }

    @Override
    public void initGameMapObject(Map originMap) {
        // todo set game map
        gameMap.setWidth(originMap.getWidth() * originMap.getTileWidth());
        gameMap.setLength(originMap.getHeight() * originMap.getTileHeight());
        gameMap.setGridWidth(originMap.getTileWidth());
        gameMap.setGridLength(originMap.getTileHeight());
        gameMap.setName(originMap.getFilename());
    }

    @Override
    public void initGameMapComponent(MapLayer layer) {
//        List<MapObject> objectList = ((ObjectGroup) layer).getObjects();
//        for (MapObject mapObject : objectList) {
//            // 1 表示所有的组件 id = 1, 暂时先写死, 不需要修改
//            AbstractGameMapComponent gameMapComponent = createComponent(mapObject.getType(), (short) 1);
//
//            if (Objects.isNull(gameMapComponent)) {
//                continue;
//            }
//
//            gameMapComponent.setId(mapObject.getId());
//            if (mapObject.getShape() instanceof Rectangle2D) {
//                Rectangle2D originShape = (Rectangle2D) mapObject.getShape();
//                gameMapComponent.setShape(new javafx.scene.shape.Rectangle(originShape.getX(),
//                        originShape.getY(), originShape.getWidth(), originShape.getHeight()));
//            } else if (mapObject.getShape() instanceof Ellipse2D) {
//                Ellipse2D originShape = (Ellipse2D) mapObject.getShape();
//                gameMapComponent.setShape(new Ellipse(originShape.getX(), originShape.getY(),
//                        originShape.getWidth(), originShape.getHeight()));
//            }
//
//            gameMapComponent.setName(mapObject.getName());
//            gameMapComponent.setAffinity(Integer.parseInt(mapObject.getProperties().getProperties().get(0).getValue()));
//            gameMapComponent.setLength(uiManager.coordinateYConvert(mapObject.getHeight()));
//            gameMapComponent.setWidth(uiManager.coordinateXConvert(mapObject.getWidth()));
//            gameMapComponent.setStartX(uiManager.coordinateXConvert(mapObject.getX()));
//            gameMapComponent.setStartY(uiManager.coordinateYConvert(mapObject.getY()));
////            gameMapComponent.setType(mapObject.getType());
//            gameMap.addMapComponent(gameMapComponent);
//        }
    }
}