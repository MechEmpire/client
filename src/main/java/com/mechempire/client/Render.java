package com.mechempire.client;

import com.mechempire.client.config.UIConfig;
import com.mechempire.client.constant.UIConstant;
import com.mechempire.client.view.AbstractView;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * package: com.mechempire.client
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午8:08
 * <p>
 * 渲染器
 */
@Slf4j
@Component
public class Render {

    @Resource
    private UIConfig uiConfig;

    private Group root = new Group();

    private HashMap<String, Canvas> canvasMap = new HashMap<>();

    @Setter
    @Getter
    private Stage stage;

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
//        stage.setScene(scene);
//        stage.setWidth(uiConfig.getWindowWidth());
//        stage.setHeight(uiConfig.getWindowHeight());
//        stage.setTitle(UIConstant.WINDOW_TITLE);
//        stage.show();
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