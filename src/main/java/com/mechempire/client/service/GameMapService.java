package com.mechempire.client.service;

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
     * 获取原始地图数据, 解析 tiled 文件
     *
     * @param mapName map 名称
     * @return 地图原始数据
     */
    Map getOriginMap(String mapName);

    /**
     * 读取地图文件, 初始化地图背景图片
     *
     * @param layer 地图层对象
     */
    void initGameMapBackground(MapLayer layer);

    /**
     * 初始化地图对象
     *
     * @param layer 地图层对象
     */
    void initGameMapTile(MapLayer layer);

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
     * @param originMap 原始地图对象
     */
    void initGameMapObject(Map originMap);

    /**
     * 初始化地图组件对象
     *
     * @param layer 地图层对象
     */
    void initGameMapComponent(MapLayer layer);
}