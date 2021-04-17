package com.mechempire.client.controller;

import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.view.GamePlayerView;
import com.mechempire.sdk.runtime.GameMap;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * package: com.mechempire.client.controller
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/12 下午8:27
 */
@Lazy
@Slf4j
@Controller
public class GamePlayerController extends AbstractController {

    @Resource
    private GamePlayerView gamePlayerView;

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Resource
    private GameMap gameMap;

    @Resource
    private ExecutorService threadPool;

    @Override
    public void show(Stage stage) {
        // 增加红方载具
//        DestroyerVehicle destroyerVehicleRed = new DestroyerVehicle();
//        destroyerVehicleRed.setId(0);
//        Rectangle rectangleRed = new Rectangle(destroyerVehicleRed.getStartX(), destroyerVehicleRed.getStartY(),
//                destroyerVehicleRed.getWidth(), destroyerVehicleRed.getLength());
//        rectangleRed.setFill(Paint.valueOf("#ff0000"));
//        destroyerVehicleRed.setShape(rectangleRed);
//        gameMap.addMapComponent(destroyerVehicleRed);
//
//        // 增加蓝方载具
//        DestroyerVehicle destroyerVehicleBlue = new DestroyerVehicle();
//        destroyerVehicleBlue.setId(4);
//        Rectangle rectangleBlue = new Rectangle(destroyerVehicleBlue.getStartX(), destroyerVehicleBlue.getStartY(),
//                destroyerVehicleBlue.getWidth(), destroyerVehicleBlue.getLength());
//        rectangleBlue.setFill(Paint.valueOf("#0000ff"));
//        destroyerVehicleBlue.setShape(rectangleBlue);
//        gameMap.addMapComponent(destroyerVehicleBlue);
        gamePlayerView.setStage(stage);

        // 连接服务器, 同步数据
        threadPool.submit(() -> {
            try {
                mechEmpireClient.run();
            } catch (Exception e) {
                log.error("client run error: {}", e.getMessage(), e);
            }
        });
    }
}