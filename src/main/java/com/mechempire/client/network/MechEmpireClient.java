package com.mechempire.client.network;

import com.mechempire.client.constant.ServerConstant;
import com.mechempire.client.network.handles.GameClientHandler;
import com.mechempire.sdk.network.CommonHeartBeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * package: com.mechempire.client.network
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/17 下午4:20
 * <p>
 * 客户端入口类
 */
@Slf4j
@Component
public class MechEmpireClient implements IClient {

    /**
     * 线程组
     */
    private final NioEventLoopGroup group = new NioEventLoopGroup(4);

    private Channel channel;

    private Bootstrap clientBootstrap;

    @Override
    public void run() {
        try {
            log.info("start to connect.");
            clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new IdleStateHandler(5, 5, 5)
                            );
                            socketChannel.pipeline().addLast(new GameClientHandler(MechEmpireClient.this));
                        }
                    });

            doConnect();
        } catch (Exception e) {
            log.error("client start error: {}", e.getMessage(), e);
        }
        sendData();
    }

    public void sendData() {
        for (int i = 0; i < 10000; i++) {
            if (null != channel && channel.isActive()) {
                String content = "client msg " + i;
                ByteBuf buf = channel.alloc().buffer(3 + content.getBytes().length);
                buf.writeShort(3 + content.getBytes().length);
                buf.writeByte(CommonHeartBeatHandler.COMMAND_MESSAGE);
                buf.writeBytes(content.getBytes(StandardCharsets.UTF_8));
                channel.writeAndFlush(buf);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doConnect() {
        if (null != channel && channel.isActive()) {
            return;
        }

        try {
            // 连接
            ChannelFuture channelFuture = clientBootstrap.connect(ServerConstant.host, ServerConstant.port).sync();
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    channel = future.channel();
                    log.info("connect to server successfully!");
                } else {
                    log.info("failed connect to server, try after 10s.");
                    future.channel().eventLoop().schedule(this::doConnect, 10, TimeUnit.SECONDS);
                }
            });

            // 等待连接关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}