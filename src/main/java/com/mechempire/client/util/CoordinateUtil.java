package com.mechempire.client.util;

import com.mechempire.client.config.UIConfig;

/**
 * package: com.mechempire.client.util
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021-01-18 10:37
 */
public class CoordinateUtil {

    public static double coordinateXConvert(double x) {
        return x * UIConfig.WINDOW_WIDTH / UIConfig.BASE_WINDOW_WIDTH;
    }


    public static double coordinateYConvert(double y) {
        return y * UIConfig.WINDOW_HEIGHT / UIConfig.BASE_WINDOW_HEIGHT;
    }
}
