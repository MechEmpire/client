package com.mechempire.client.network.handles;

import com.mechempire.client.network.MechEmpireClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
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
public class GameClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public GameClientHandler() {
        super(false);
    }

    @Resource
    private MechEmpireClient mechEmpireClient;

    /**
     * 建立连接, 准备进行通信时会调用
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active, send message to server.");
        ctx.writeAndFlush(Unpooled.copiedBuffer("Tairy", CharsetUtil.UTF_8)).sync();
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        log.info("client received message: {}", byteBuf.toString(CharsetUtil.UTF_8));
//        ctx.write(Unpooled.copiedBuffer("come on babe.", CharsetUtil.UTF_8));
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
                ctx.writeAndFlush(Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8)).sync();
            } else {
                mechEmpireClient.doConnect();
            }
        }
    }
}