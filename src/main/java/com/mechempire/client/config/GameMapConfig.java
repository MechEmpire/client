package com.mechempire.client.config;

import com.mechempire.sdk.runtime.GameMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * package: com.mechempire.client.config
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/3/28 上午11:58
 */
@Configuration
public class GameMapConfig {

    @Bean
    public GameMap gameMap() {
        return new GameMap();
    }
}