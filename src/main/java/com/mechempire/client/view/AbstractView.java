package com.mechempire.client.view;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Data;

/**
 * package: com.mechempire.client.view
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/1/31 下午7:59
 */
@Data
abstract public class AbstractView {

    protected Pane root;

    public static void makeFadeTransition(Node node, int millis, double fromValue, double toValue) {
        FadeTransition ft = new FadeTransition(Duration.millis(millis));
        ft.setFromValue(fromValue);
        ft.setToValue(toValue);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true);
        ft.setNode(node);
        ft.play();
    }

    public static void makeScaleTransition(Node node, int millis, double byX, double byY) {
        ScaleTransition st = new ScaleTransition(Duration.millis(millis));
        st.setByX(byX);
        st.setByY(byY);
        st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true);
        st.setNode(node);
        st.play();
    }

    public static void makeRotateTransition(Node node, int mills, double byAngle) {
        RotateTransition rt = new RotateTransition(Duration.millis(mills));
        rt.setByAngle(byAngle);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setNode(node);
        rt.play();
    }

    public static void makeRotateTransition(Node node, int mills, double fromAngle, double toAngle, boolean autoReverse) {
        RotateTransition rt = new RotateTransition(Duration.millis(mills));
        rt.setFromAngle(fromAngle);
        rt.setToAngle(toAngle);
        rt.setAutoReverse(autoReverse);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setNode(node);
        rt.play();
    }
}