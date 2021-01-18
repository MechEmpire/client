package com.mechempire.client.config;

/**
 * package: com.mechempire.client.config
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:37
 */
public class UIConfig {


    public static final double BASE_WINDOW_WIDTH = 1280;

    public static final double BASE_WINDOW_HEIGHT = 1280;

    /**
     * 客户端窗口宽度
     */
    public static final double WINDOW_WIDTH = 640;

    /**
     * 客户端窗口高度
     */
    public static final double WINDOW_HEIGHT = 640;

    /**
     * 客户端窗口标题
     */
    public static final String WINDOW_TITLE = "MechEmpire - 机甲帝国";

    /**
     * 主页面背景样式
     */
    public static final String MAIN_SCENE_BACKGROUND = "-fx-background-image: url('background.jpg'); -fx-background-repeat: stretch; -fx-background-position: center center; -fx-background-size: 100% 100%; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);";

    public static final double MAIN_LOGO_FIT_WIDTH = WINDOW_WIDTH * 0.5;

    public static final double MAIN_LOGO_FIT_HEIGHT = WINDOW_HEIGHT * 0.3;

    public static final double MAIN_LOGO_X = WINDOW_WIDTH * 0.25;

    public static final double MAIN_LOGO_Y = WINDOW_HEIGHT * 0.25;

    public static final String START_BTN_STYLE = "-fx-background-color: linear-gradient(to top,#000000,#525252);";

    public static final double START_BTN_PREF_HEIGHT = WINDOW_HEIGHT * 0.06;

    public static final double START_BTN_PREF_WIGHT = WINDOW_WIDTH * 0.15;

    public static final double START_BTN_X = WINDOW_WIDTH * 0.4;

    public static final double START_BTN_Y = WINDOW_HEIGHT * 0.7;

    public static final double START_BTN_FONT_SIZE = WINDOW_WIDTH * 0.025;
}