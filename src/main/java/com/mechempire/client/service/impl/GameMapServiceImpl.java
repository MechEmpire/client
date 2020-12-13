package com.mechempire.client.service.impl;

import com.mechempire.client.service.GameMapService;
import com.mechempire.sdk.runtime.GameMap;
import org.mapeditor.core.Map;

/**
 * package: com.mechempire.client.service.impl
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/13 下午3:15
 */
public class GameMapServiceImpl implements GameMapService {
    @Override
    public GameMap initGameMap(Map originMap) {
        GameMap gameMap = new GameMap();

        gameMap.setWidth(originMap.getWidth() * originMap.getTileWidth())
                .setHeight(originMap.getHeight() * originMap.getTileHeight())
                .setName(originMap.getFilename())
                .setGridWidth(originMap.getTileWidth())
                .setGridHeight(originMap.getTileHeight());
        return gameMap;
    }
}