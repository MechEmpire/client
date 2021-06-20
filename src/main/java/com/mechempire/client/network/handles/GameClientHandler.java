package com.mechempire.client.network.handles;

import com.google.protobuf.Any;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.client.service.GameMapService;
import com.mechempire.client.view.GamePlayerView;
import com.mechempire.sdk.constant.MapComponent;
import com.mechempire.sdk.constant.MapImageElementType;
import com.mechempire.sdk.core.component.MapImageElement;
import com.mechempire.sdk.core.game.AbstractGameMapComponent;
import com.mechempire.sdk.proto.CommonDataProto;
import com.mechempire.sdk.runtime.GameMap;
import com.mechempire.sdk.runtime.Position2D;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import javafx.application.Platform;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.mechempire.sdk.core.factory.GameMapComponentFactory.createComponent;

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

            engineWorld.getGameMap().getMapImageElementList().forEach(imageElement -> gameMap.getImageElementList().add(
                    new MapImageElement(
                            imageElement.getSource(),
                            imageElement.getOffsetX(),
                            imageElement.getOffsetY(),
                            imageElement.getWidth(),
                            imageElement.getHeight(),
                            imageElement.getOpacity(),
                            MapImageElementType.valueOf(imageElement.getImageType().name())
                    )
            ));

            engineWorld.getGameMap().getComponentsMap().forEach((id, component) -> {
                try {
                    MapComponent mapComponent = MapComponent.valueOf(component.getType());
                    AbstractGameMapComponent gameMapComponent = createComponent(mapComponent.getClazz());
                    if (Objects.isNull(gameMapComponent)) {
                        return;
                    }
                    gameMapComponent.setMapComponent(mapComponent);
                    if (component.getShape().equals(CommonDataProto.GameMap.MapComponent.ComponentShape.RECTANGLE2D)) {
                        gameMapComponent.setShape(
                                new Rectangle(
                                        component.getStartX(),
                                        component.getStartY(),
                                        component.getWidth(),
                                        component.getLength()
                                )
                        );
                    } else if (component.getShape().equals(CommonDataProto.GameMap.MapComponent.ComponentShape.ELLIPSE2D)) {
                        double radiusX = component.getWidth() / 2.0 + 1.0;
                        double radiusY = component.getLength() / 2.0 + 1.0;
                        gameMapComponent.setShape(
                                new Ellipse(
                                        component.getStartX() + radiusX,
                                        component.getStartY() + radiusY,
                                        radiusX,
                                        radiusY
                                )
                        );
                    }
                    gameMapComponent.setId(component.getId());
                    gameMapComponent.setName(component.getName());
                    gameMapComponent.setAffinity(component.getAffinity());
                    gameMapComponent.setLength(component.getLength());
                    gameMapComponent.setWidth(component.getWidth());
                    gameMapComponent.setStartX(component.getStartX());
                    gameMapComponent.setStartY(component.getStartY());
                    gameMapComponent.setPosition(new Position2D(component.getPosition().getX(), component.getPosition().getY()));
                    gameMap.addMapComponent(gameMapComponent);
                } catch (Exception e) {
                    log.error("init map component, id: {}, component: {} error: {}", id, component, e.getMessage(), e);
                }
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
                AbstractGameMapComponent gameMapComponent = gameMap.getMapComponent(message.getComponentId());
                if (Objects.isNull(gameMapComponent)) {
                    continue;
                }
                gameMapComponent.getPosition().setX(message.getPositionX());
                gameMapComponent.getPosition().setY(message.getPositionY());
                Platform.runLater(() -> gamePlayerView.updateMechView(gameMapComponent));
            }
        }
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