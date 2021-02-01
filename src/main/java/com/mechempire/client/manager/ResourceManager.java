package com.mechempire.client.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * package: com.mechempire.client.manager
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:51
 */
@Slf4j
@Component
public class ResourceManager {

    private HashMap<String, String> fonts = new HashMap<>(16);

    private HashMap<String, String> images = new HashMap<>(16);

    private HashMap<String, String> media = new HashMap<>(16);
}