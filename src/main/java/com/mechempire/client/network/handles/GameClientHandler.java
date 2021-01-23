package com.mechempire.client.network.handles;

import com.mechempire.client.network.MechEmpireClient;
import com.mechempire.sdk.proto.ResultMessageProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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

    private ResultMessageProto.CommonData.Builder builder = ResultMessageProto.CommonData.newBuilder();

    /**
     * 建立连接, 准备进行通信时会调用
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active, send message to server.");
        builder = ResultMessageProto.CommonData.newBuilder();
        builder.setMessage("init");
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResultMessageProto.CommonData commonData = (ResultMessageProto.CommonData) msg;
        if (null == commonData) {
            return;
        }

        System.out.println(commonData.getMessage());

        if (commonData.getMessage().equals("init")) {
            builder = ResultMessageProto.CommonData.newBuilder();
            builder.setMessage("start");
            ctx.writeAndFlush(builder.build());
        }

        if (commonData.getData().getValue().isEmpty()) {
            return;
        }

        ResultMessageProto.ResultMessageList messageList =
                commonData.getData().unpack(ResultMessageProto.ResultMessageList.class);

        System.out.println("======");
        for (int i = 0; i < messageList.getResultMessageCount(); i++) {
            ResultMessageProto.ResultMessage message = messageList.getResultMessage(i);
            System.out.printf("component_id: %d, position_x: %.2f, position_y: %.2f\n",
                    message.getComponentId(),
                    message.getPositionX(),
                    message.getPositionY()
            );
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
                builder = ResultMessageProto.CommonData.newBuilder();
                builder.setMessage("ping");
                ctx.writeAndFlush(builder.build());
            } else {
                mechEmpireClient.doConnect();
            }
        }
    }
}