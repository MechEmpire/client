package com.mechempire.client.view;

import com.google.common.collect.Maps;
import com.mechempire.client.manager.ResourceManager;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.constant.MapComponentConstant;
import com.mechempire.sdk.constant.MapImageElementType;
import com.mechempire.sdk.constant.TeamAffinity;
import com.mechempire.sdk.core.game.AbstractGameMapComponent;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentMap;

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

    @Resource
    private ResourceManager resourceManager;

    private final ConcurrentMap<String, Canvas> canvasMap = Maps.newConcurrentMap();

    @Override
    public void render() {
        try {
            root = new Pane();
            Canvas mapCanvas = getCanvas("map");
            Canvas commonCanvas = getCanvas("common");
            // 绘制地图
            gameMap.getImageElementList().forEach(mapImageElement -> {
                if (mapImageElement.getImageType().equals(MapImageElementType.BACKGROUND)) {
                    GraphicsContext gc = mapCanvas.getGraphicsContext2D();
                    Image image = resourceManager.getImage(mapImageElement.getSource());
                    gc.drawImage(image, 0, 0, uiManager.getWindowWidth(), uiManager.getWindowHeight());
                } else if (mapImageElement.getImageType().equals(MapImageElementType.LOGO)) {
                    GraphicsContext gc = mapCanvas.getGraphicsContext2D();
                    Image image = resourceManager.getImage(mapImageElement.getSource());
                    if (mapImageElement.getOpacity() > 0.0) {
                        gc.setGlobalAlpha(mapImageElement.getOpacity());
                    }
                    gc.drawImage(image, mapImageElement.getOffsetX(),
                            mapImageElement.getOffsetY(), mapImageElement.getWidth(), mapImageElement.getHeight());
                } else {
                    GraphicsContext gc = commonCanvas.getGraphicsContext2D();
                    Image image = resourceManager.getImage(mapImageElement.getSource());
                    gc.drawImage(image, mapImageElement.getOffsetX(),
                            mapImageElement.getOffsetY(), mapImageElement.getWidth(), mapImageElement.getHeight());
                }
            });

            // 绘制机甲
            gameMap.getComponents().forEach((id, component) -> {
                if (!MapComponentConstant.COMPONENT_VEHICLE.getName().equalsIgnoreCase(component.getMapComponent().getType())) {
                    return;
                }
                if (component.getAffinity() == TeamAffinity.BLUE.getCode()) {
                    Canvas blueSpriteCanvas = getCanvas("blue_sprite");
                    GraphicsContext blueGc = blueSpriteCanvas.getGraphicsContext2D();
                    blueGc.rect(component.getPosition().getX(),
                            component.getPosition().getY(), component.getWidth(), component.getLength());
                    blueGc.setFill(Color.BLUE);
                    blueGc.fill();
                } else if (component.getAffinity() == TeamAffinity.RED.getCode()) {
                    Canvas redSpriteCanvas = getCanvas("red_sprite");
                    GraphicsContext redGc = redSpriteCanvas.getGraphicsContext2D();
                    redGc.rect(component.getPosition().getX(),
                            component.getPosition().getY(), component.getWidth(), component.getLength());
                    redGc.setFill(Color.RED);
                    redGc.fill();
                }
            });

            uiManager.initCommonStage(stage);
            stage.setScene(new Scene(root, uiManager.getWindowWidth(), uiManager.getWindowHeight()));
            stage.show();
        } catch (Exception e) {
            log.error("init map error: {}", e.getMessage(), e);
        }
    }

    /**
     * 更新组件画布
     *
     * @param component 组件对象
     */
    public void updateMechView(AbstractGameMapComponent component) {
        // 绘制机甲
        if (!MapComponentConstant.COMPONENT_VEHICLE.getName().equalsIgnoreCase(component.getMapComponent().getType())) {
            return;
        }
        if (component.getAffinity() == TeamAffinity.BLUE.getCode()) {
            Canvas blueSpriteCanvas = getCanvas("blue_sprite");
            GraphicsContext blueGc = blueSpriteCanvas.getGraphicsContext2D();
            blueGc.beginPath();
            blueGc.moveTo(component.getPosition().getX(), component.getPosition().getY());
        } else if (component.getAffinity() == TeamAffinity.RED.getCode()) {
            Canvas redSpriteCanvas = getCanvas("red_sprite");
            GraphicsContext redGc = redSpriteCanvas.getGraphicsContext2D();
            redGc.beginPath();
            redGc.moveTo(component.getPosition().getX(), component.getPosition().getY());
        }
        log.info("positionX: {}, Y: {}", component.getPosition().getX(), component.getPosition().getY());
    }

    private Canvas getCanvas(String value) {
        Canvas canvas;
        if (canvasMap.containsKey(value)) {
            canvas = canvasMap.get(value);
        } else {
            canvas = new Canvas();
            canvasMap.put(value, canvas);
            canvas.widthProperty().bind(stage.widthProperty());
            canvas.heightProperty().bind(stage.heightProperty());
            root.getChildren().add(canvas);
        }

        // 画布擦除
        log.info("canvasW: {}, canvasH: {}", canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return canvas;
    }
}
