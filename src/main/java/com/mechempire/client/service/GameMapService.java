package com.mechempire.client.service;

import com.mechempire.sdk.runtime.GameMap;
import org.mapeditor.core.Map;

/**
 * package: com.mechempire.client.service
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午3:14
 */
public interface GameMapService {

    /**
     * 初始化游戏地图
     *
     * @return 游戏地图对象
     */
    GameMap initGameMap(Map originMap);
}