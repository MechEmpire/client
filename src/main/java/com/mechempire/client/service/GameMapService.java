package com.mechempire.client.service;

import com.mechempire.sdk.runtime.GameMap;
import javafx.scene.layout.Pane;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;

/**
 * package: com.mechempire.client.service
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午3:14
 */
public interface GameMapService {

    /**
     * 读取地图文件, 初始化地图背景图片
     *
     * @param layer        地图层对象
     * @param mapContainer 地图容器对象
     */
    void initGameMapBackground(MapLayer layer, Pane mapContainer);

    /**
     * 初始化地图 logo, 后续可以将背景图和 logo 合并
     *
     * @param layer        地图层对象
     * @param mapContainer 地图容器对象
     */
    void initGameMapLogo(MapLayer layer, Pane mapContainer);

    /**
     * 初始化 tile 层
     *
     * @param originMap    原始数据对象
     * @param layer        地图层对象
     * @param mapContainer 地图容器对象
     */
    void initTileLayer(Map originMap, MapLayer layer, Pane mapContainer);

    /**
     * 初始化游戏地图
     *
     * @return 游戏地图对象
     */
    GameMap initGameMapObject(Map originMap);

    /**
     * 初始化地图组件对象
     *
     * @param layer   地图层对象
     * @param gameMap 地图对象
     */
    void initGameMapComponent(MapLayer layer, GameMap gameMap);
}