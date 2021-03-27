package com.mechempire.client.network.handles;

import com.google.protobuf.Any;
import com.mechempire.client.manager.UIManager;
import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.sdk.core.component.DestroyerVehicle;
import com.mechempire.sdk.proto.CommonDataProto;
import com.mechempire.sdk.runtime.GameMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * package: com.mechempire.client.network.handles
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/17 下午4:37
 */
@Slf4j
@Component
public class GameClientHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private MechEmpireClient mechEmpireClient;

    @Resource
    private UIManager uiManager;

    @Resource
    private GameMap gameMap;

    private CommonDataProto.CommonData.Builder builder = CommonDataProto.CommonData.newBuilder();

    /**
     * 建立连接, 准备进行通信时会调用
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active, send message to server.");
        builder = CommonDataProto.CommonData.newBuilder();
        builder.setMessage("init");

        CommonDataProto.InitRequest.Builder initRequestBuild = CommonDataProto.InitRequest.newBuilder();
        initRequestBuild.setScreenHeight(uiManager.getScreenHeight());
        initRequestBuild.setScreenWidth(uiManager.getScreenWidth());
        builder.setData(Any.pack(initRequestBuild.build()));
        ctx.writeAndFlush(builder.build());
        builder.clear();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CommonDataProto.CommonData commonData = (CommonDataProto.CommonData) msg;
        if (null == commonData) {
            return;
        }

        System.out.println(commonData.getMessage());

        if (commonData.getMessage().equals("init")) {
            builder = CommonDataProto.CommonData.newBuilder();
            builder.setMessage("start");
            ctx.writeAndFlush(builder.build());
            builder.clear();
        }

        if (commonData.getData().getValue().isEmpty()) {
            return;
        }

        CommonDataProto.ResultMessageList messageList =
                commonData.getData().unpack(CommonDataProto.ResultMessageList.class);

        DestroyerVehicle destroyerVehicle = (DestroyerVehicle) gameMap.getMapComponent(111);

        System.out.println("======");
        for (int i = 0; i < messageList.getResultMessageCount(); i++) {
            CommonDataProto.ResultMessage message = messageList.getResultMessage(i);
            Shape shape = destroyerVehicle.getShape();
//            shape.setLayoutX(message.getPositionX());
//            shape.setLayoutY(message.getPositionY());
            if (shape instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) shape;
                rectangle.setX(message.getPositionX());
                rectangle.setY(message.getPositionY());
            }

            System.out.printf("component_id: %d, position_x: %.2f, position_y: %.2f\n",
                    message.getComponentId(),
                    message.getPositionX(),
                    message.getPositionY()
            );
        }
        System.out.println("======");
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
                builder = CommonDataProto.CommonData.newBuilder();
                builder.setMessage("ping");
                ctx.writeAndFlush(builder.build());
                builder.clear();
            } else {
                mechEmpireClient.doConnect();
            }
        }
    }
}