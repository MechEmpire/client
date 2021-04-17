package com.mechempire.client.network.handles;

import com.google.protobuf.Any;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.view.GamePlayerView;
import com.mechempire.sdk.core.component.DestroyerVehicle;
import com.mechempire.sdk.proto.CommonDataProto;
import com.mechempire.sdk.runtime.GameMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import javafx.application.Platform;
import javafx.scene.shape.Shape;
import lombok.extern.slf4j.Slf4j;
import org.mapeditor.core.ImageLayer;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * package: com.mechempire.client.network.handles
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/17 下午4:37
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class GameClientHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Resource
    private GameMapService gameMapService;

    @Resource
    private UIManager uiManager;

    @Resource
    private GamePlayerView gamePlayerView;

    @Resource
    private GameMap gameMap;

    private final CommonDataProto.CommonData.Builder builder = CommonDataProto.CommonData.newBuilder();

    /**
     * 建立连接, 准备进行通信时会调用
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active, send message to server.");
        builder.clear();
        builder.setMessage("init");
        builder.setCommand(CommonDataProto.CommonData.CommandEnum.INIT);
        CommonDataProto.InitRequest.Builder initRequestBuild = CommonDataProto.InitRequest.newBuilder();
        initRequestBuild.setScreenHeight(uiManager.getScreenHeight());
        initRequestBuild.setScreenWidth(uiManager.getScreenWidth());
        builder.setData(Any.pack(initRequestBuild.build()));
        ctx.writeAndFlush(builder.build());
        log.info("send init.");
        builder.clear();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CommonDataProto.CommonData commonData = (CommonDataProto.CommonData) msg;
        if (Objects.isNull(commonData)) {
            return;
        }

        if (commonData.getCommand().equals(CommonDataProto.CommonData.CommandEnum.INIT)) {
            builder.clear();
            builder.setCommand(CommonDataProto.CommonData.CommandEnum.START);
            builder.setMessage("start");
            ctx.writeAndFlush(builder.build());

            CommonDataProto.EngineWorld engineWorld = commonData.getData().unpack(CommonDataProto.EngineWorld.class);
            Map originMap = gameMapService.getOriginMap(engineWorld.getGameMap().getMapName());
            gameMapService.initGameMapObject(originMap);

//            log.info("components: {}", engineWorld.getComponentsMap());

            // init map
            MapLayer layer;
            // 初始化地图组件
            for (int i = 0; i < originMap.getLayerCount(); i++) {
                layer = originMap.getLayer(i);

                if (layer instanceof ImageLayer) {
                    if ("background".equals(layer.getName())) {
                        gameMapService.initGameMapBackground(layer);
                    } else {
                        gameMapService.initGameMapTile(layer);
                    }
                }
            }

            log.info("engineWorld: {}", engineWorld);

            engineWorld.getGameMap().getComponentsMap().forEach((id, component) -> {
//                AbstractGameMapComponent gameMapComponent = createComponent(component.getType(), (short) 1);
//                if (Objects.isNull(gameMapComponent)) {
//                    return;
//                }
//                gameMapComponent.setId(id);
//                if (component.getShape().equals(CommonDataProto.MapComponent.ComponentShape.RECTANGLE2D)) {
//                    gameMapComponent.setShape(new Rectangle(component.getStartX(),
//                            component.getStartY(), component.getWidth(), component.getLength()));
//                } else if (component.getShape().equals(CommonDataProto.MapComponent.ComponentShape.ELLIPSE2D)) {
//                    gameMapComponent.setShape(new Ellipse(component.getStartX(),
//                            component.getStartY(), component.getWidth(), component.getLength()));
//                }
//
//                gameMapComponent.setName(component.getName());
//                gameMapComponent.setAffinity(component.getAffinity());
//                gameMapComponent.setLength(uiManager.coordinateYConvert(component.getLength()));
//                gameMapComponent.setWidth(uiManager.coordinateXConvert(component.getWidth()));
//                gameMapComponent.setStartX(uiManager.coordinateXConvert(component.getStartX()));
//                gameMapComponent.setStartY(uiManager.coordinateYConvert(component.getStartY()));
//                gameMapComponent.setType(component.getType());
//                gameMap.addMapComponent(gameMapComponent);
            });

            Platform.runLater(() -> gamePlayerView.render());
        } else if (commonData.getCommand().equals(CommonDataProto.CommonData.CommandEnum.RUNNING)) {
            if (commonData.getData().getValue().isEmpty()) {
                return;
            }

            CommonDataProto.ResultMessageList messageList =
                    commonData.getData().unpack(CommonDataProto.ResultMessageList.class);

            for (int i = 0; i < messageList.getResultMessageCount(); i++) {
                CommonDataProto.ResultMessageList.ResultMessage message = messageList.getResultMessage(i);
                if (gameMap.getMapComponent(message.getComponentId()) instanceof DestroyerVehicle) {
                    DestroyerVehicle destroyerVehicle = (DestroyerVehicle) gameMap.getMapComponent(message.getComponentId());
                    if (Objects.nonNull(destroyerVehicle)) {
                        Shape shape = destroyerVehicle.getShape();
                        shape.setTranslateX(message.getPositionX());
                        shape.setTranslateY(message.getPositionY());
                    }
                }
            }
        }
//        System.out.println("======");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (!(cause instanceof IOException)) {
            log.error("client exception: {}", cause.getMessage(), cause);
        }
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.info("channel unregistered");
        ctx.channel().eventLoop().schedule(() -> {
            log.info("reconnect to server.");
            mechEmpireClient.doConnect();
        }, 10, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("channel inactive.");
        ctx.flush();
        ctx.channel().eventLoop().schedule(() -> {
            log.info("reconnect to server.");
            mechEmpireClient.doConnect();
        }, 10, TimeUnit.SECONDS);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }

        IdleStateEvent e = (IdleStateEvent) evt;
        if (e.state() == IdleState.READER_IDLE) {
            if (null != ctx.channel() && ctx.channel().isActive()) {
                builder.clear();
                builder.setMessage("ping");
                builder.setCommand(CommonDataProto.CommonData.CommandEnum.PING);
                ctx.writeAndFlush(builder.build());
                builder.clear();
            } else {
                mechEmpireClient.doConnect();
            }
        }
    }
}