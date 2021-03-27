package com.mechempire.client;

import com.google.common.collect.Maps;
import com.mechempire.client.constant.UIConstant;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.view.AbstractView;
import com.mechempire.client.view.GamePlayerView;
import com.mechempire.client.view.HomeView;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:08
 * <p>
 * 渲染器, 负责界面渲染
 */
@Lazy
@Slf4j
@Component
public class Render {

    @Resource
    private UIManager uiManager;

    private final Group root = new Group();

    private final HashMap<String, Canvas> canvasMap = Maps.newHashMap();

    @Setter
    @Getter
    private Stage stage;

    @Resource
    private HomeView homeView;

    @Resource
    private GamePlayerView gamePlayerView;

    /**
     * 初始化渲染器
     */
    public void init() {
        init(root);
    }

    public void init(AbstractView view) {
        init(view.getRoot());
    }

    public void init(Parent parent) {
        init(new Scene(parent));
    }

    public void init(Scene scene) {
        stage.setScene(scene);
        stage.setWidth(uiManager.getWindowWidth());
        stage.setHeight(uiManager.getWindowHeight());
        stage.setTitle(UIConstant.WINDOW_TITLE);
        stage.show();
    }

    /**
     * 显示首页
     */
    public void home() {
        init(homeView);
    }

    /**
     * 显示游戏地图页面
     */
    public void game() {
        init(gamePlayerView);
    }

    /**
     * 获取画布
     *
     * @param value value
     * @return 画布对象
     */
    private Canvas getCanvas(String value) {
        Canvas canvas;

        if (canvasMap.containsKey(value)) {
            canvas = canvasMap.get(value);
        } else {
            canvas = new Canvas();
            canvas.widthProperty().bind(stage.widthProperty());
            canvas.heightProperty().bind(stage.heightProperty());
            canvasMap.put(value, canvas);
            root.getChildren().add(canvas);
        }

        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        return canvas;
    }

    public void update() {

    }
}