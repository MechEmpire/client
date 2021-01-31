package com.mechempire.client.config;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * package: com.mechempire.client.config
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:37
 * <p>
 * UI 相关参数处理类
 */
@Data
@Component
public class UIConfig {
    /**
     * 最大窗口宽度, 用于坐标计算的 base 值
     */
    public static final double MAX_WINDOW_WIDTH = 1280;

    /**
     * 最大窗口高度, 用于坐标计算的 base 值
     */
    public static final double MAX_WINDOW_HEIGHT = 1280;

    /**
     * 窗口顶部 bar 高度
     */
    public static final double WINDOW_BAR_HEIGHT = 20;

    /**
     * 客户端窗口标题
     */
    public static final String WINDOW_TITLE = "MechEmpire - 机甲帝国";

    /**
     * 主页面背景样式
     */
    public static final String MAIN_SCENE_BACKGROUND = "-fx-background-image: url('background.jpg'); -fx-background-repeat: stretch; -fx-background-position: center center; -fx-background-size: 100% 100%; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);";

    /**
     * 开始按钮样式
     */
    public static final String START_BTN_STYLE = "-fx-background-color: linear-gradient(to top,#000000,#525252);";

    /**
     * 窗口宽度
     */
    private double windowWidth;

    /**
     * 窗口高度
     */
    private double windowHeight;

    /**
     * 屏幕宽度
     */
    private double screenWidth;

    /**
     * 屏幕高度
     */
    private double screenHeight;

    /**
     * logo 填充宽
     */
    private double mainLogoFitWidth;

    /**
     * logo 填充高
     */
    private double mainLogoFitHeight;

    /**
     * logo x
     */
    private double mainLogoX;

    /**
     * logo y
     */
    private double mainLogoY;

    /**
     * btn pref width
     */
    private double startBtnPrefWidth;

    /**
     * btn pref height
     */
    private double startBtnPrefHeight;

    /**
     * start btn x
     */
    private double startBtnX;

    /**
     * start btn y
     */
    private double startBtnY;

    /**
     * start btn font size
     */
    private double startBtnFontSize;

    /**
     * construct
     */
    public UIConfig() {
        Rectangle2D screen = Screen.getPrimary().getBounds();
        this.screenWidth = screen.getWidth();
        this.screenHeight = screen.getHeight();

        if (this.screenWidth <= 900) {
            this.windowWidth = 640;
            this.windowHeight = 640;
        } else {
            this.windowWidth = 1280;
            this.windowHeight = 1280;
        }

        this.mainLogoFitWidth = windowWidth * 0.5;
        this.mainLogoFitHeight = windowHeight * 0.3;
        this.mainLogoX = windowWidth * 0.25;
        this.mainLogoY = windowHeight * 0.25;
        this.startBtnPrefWidth = windowWidth * 0.15;
        this.startBtnPrefHeight = windowHeight * 0.06;
        this.startBtnX = windowWidth * 0.4;
        this.startBtnY = windowHeight * 0.7;
        this.startBtnFontSize = windowWidth * 0.025;
    }

    /**
     * 坐标转换
     *
     * @param x x
     * @return new x
     */
    public double coordinateXConvert(double x) {
        return x * this.windowWidth / MAX_WINDOW_WIDTH;
    }

    /**
     * 坐标转换
     *
     * @param y y
     * @return new y
     */
    public double coordinateYConvert(double y) {
        return y * this.windowHeight / MAX_WINDOW_HEIGHT;
    }

    /**
     * init common stage
     *
     * @param stage stage
     */
    public void initCommonStage(Stage stage) {
        stage.setTitle(WINDOW_TITLE);
        stage.setMinWidth(this.windowWidth);
        stage.setMinHeight(this.windowHeight + WINDOW_BAR_HEIGHT);
        stage.setMaxWidth(this.windowWidth);
        stage.setMaxHeight(this.windowHeight + WINDOW_BAR_HEIGHT);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));
    }
}