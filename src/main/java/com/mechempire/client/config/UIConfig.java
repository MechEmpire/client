package com.mechempire.client.config;

import com.mechempire.client.constant.UIConstant;
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

        if (this.screenWidth <= 1440) {
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
        return x * this.windowWidth / UIConstant.MAX_WINDOW_WIDTH;
    }

    /**
     * 坐标转换
     *
     * @param y y
     * @return new y
     */
    public double coordinateYConvert(double y) {
        return y * this.windowHeight / UIConstant.MAX_WINDOW_HEIGHT;
    }

    /**
     * init common stage
     *
     * @param stage stage
     */
    public void initCommonStage(Stage stage) {
        stage.setTitle(UIConstant.WINDOW_TITLE);
        stage.setMinWidth(this.windowWidth);
        stage.setMinHeight(this.windowHeight + UIConstant.WINDOW_BAR_HEIGHT);
        stage.setMaxWidth(this.windowWidth);
        stage.setMaxHeight(this.windowHeight + UIConstant.WINDOW_BAR_HEIGHT);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/image/logo.png")));
    }
}