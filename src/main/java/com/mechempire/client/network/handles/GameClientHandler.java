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

/**
 * package: com.mechempire.client.network.handles
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/17 下午4:37
 */
@Slf4j
@Component
public class GameClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private MechEmpireClient mechEmpireClient;

    public GameClientHandler(MechEmpireClient mechEmpireClient) {
        this.mechEmpireClient = mechEmpireClient;
    }

    /**
     * 建立连接, 准备进行通信时会调用
     *
     * @param ctx 上下文
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel active, send message to server.");
        log.info("channel_is_active_in_active: {}", ctx.channel().isActive());

//        Thread.sleep(10000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("Tairy", CharsetUtil.UTF_8)).sync();
        ctx.flush();
        ctx.writeAndFlush(Unpooled.copiedBuffer("Tairy1", CharsetUtil.UTF_8)).sync();
        ctx.flush();
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Tairy2", CharsetUtil.UTF_8)).sync();
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Tairy3", CharsetUtil.UTF_8)).sync();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        log.info("client received message: {}", byteBuf.toString(CharsetUtil.UTF_8));
        log.info("channel_is_active_in_read0: {}", ctx.channel().isActive());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client exception: {}", cause.getMessage(), cause);
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channel read complete.");
        log.info("channel_is_active_in_read_complete: {}", ctx.channel().isActive());
        ctx.flush();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        log.info("channel unregistered");
//        ctx.channel().eventLoop().schedule(() -> {
//            log.info("reconnect to server.");
//            mechEmpireClient.doConnect();
//        }, 10, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channel inactive.");
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }

        IdleStateEvent e = (IdleStateEvent) evt;
        if (e.state() == IdleState.READER_IDLE) {
            ctx.writeAndFlush(Unpooled.copiedBuffer("ping", CharsetUtil.UTF_8)).sync();
            ctx.flush();
            log.info("disconnection due to no inbound traffic.");
            Thread.sleep(10000);
            ctx.close();
        }
    }
}