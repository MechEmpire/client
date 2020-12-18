package com.mechempire.client.network;

import com.mechempire.client.network.handles.GameClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

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
    @Override
    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            clientBootstrap.remoteAddress(new InetSocketAddress("localhost", 6666));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    log.info("client trigger connect.");
                    socketChannel.pipeline().addLast(new GameClientHandler());
                }
            });

            log.info("server connected!");
            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("connect server error: {}", e.getMessage(), e);
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}