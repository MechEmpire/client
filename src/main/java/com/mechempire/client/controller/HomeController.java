package com.mechempire.client.controller;

import com.mechempire.client.Render;
import com.mechempire.client.view.HomeView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2021/3/27 下午10:14
 */
@Lazy
@Slf4j
@Controller
public class HomeController extends AbstractController {

    @Resource
    private Render render;

    @Resource
    private HomeView homeView;

    @Override
    public void show(Stage stage) {
        // todo deal with logic params.
        homeView.setStage(stage);
        homeView.render();
    }
}