package com.mechempire.client.network;

import com.mechempire.client.constant.ServerConstant;
import com.mechempire.client.network.handles.GameClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public void run() throws Exception {
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
                                    new IdleStateHandler(20, 20, 20)
                            );
                            GameClientHandler gameClientHandler = new GameClientHandler();
                            socketChannel.pipeline().addLast(gameClientHandler);
                        }
                    });

            doConnect();
        } catch (Exception e) {
            log.error("client start error: {}", e.getMessage(), e);
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public void doConnect() {
        if (null != channel && channel.isActive()) {
            return;
        }

        try {
            // 连接
            ChannelFuture channelFuture =
                    clientBootstrap.connect(ServerConstant.host, ServerConstant.port).sync();
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