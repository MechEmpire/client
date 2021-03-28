package com.mechempire.client.view;

import com.mechempire.client.manager.UIManager;
import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.core.component.DestroyerVehicle;
import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;
import org.mapeditor.core.ImageLayer;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.ObjectGroup;
import org.mapeditor.io.TMXMapReader;
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

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Override
    public void render() {
        try {
            root = new Pane();
            // map reader
            TMXMapReader mapReader = new TMXMapReader();
            Map originMap = mapReader.readMap(getClass().getResource("/map/map_v1.tmx").toString());
            MapLayer layer;

            gameMapService.initGameMapObject(originMap);

            // 初始化地图组件
            for (int i = 0; i < originMap.getLayerCount(); i++) {
                layer = originMap.getLayer(i);

                if (layer instanceof ImageLayer) {
                    if (layer.getName().equals("background")) {
                        gameMapService.initGameMapBackground(layer, root);
                    } else {
                        gameMapService.initGameMapTile(layer, root);
                    }
                } else if (layer instanceof ObjectGroup) {
                    gameMapService.initGameMapComponent(layer);
                }
            }

            DestroyerVehicle destroyerVehicleRed = (DestroyerVehicle) gameMap.getMapComponent(0);
            DestroyerVehicle destroyerVehicleBlue = (DestroyerVehicle) gameMap.getMapComponent(4);
            root.getChildren().add(destroyerVehicleRed.getShape());
            root.getChildren().add(destroyerVehicleBlue.getShape());

            // 连接服务器, 同步数据
            new Thread(() -> {
                try {
                    mechEmpireClient.run();
                } catch (Exception e) {
                    log.error("client run error: {}", e.getMessage(), e);
                }
            }).start();

            uiManager.initCommonStage(stage);
            stage.setScene(new Scene(root, uiManager.getWindowWidth(), uiManager.getWindowHeight()));
            stage.show();
        } catch (Exception e) {
            log.error("init map error: {}", e.getMessage(), e);
        }
    }
}
