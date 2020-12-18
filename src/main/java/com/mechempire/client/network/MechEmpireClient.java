package com.mechempire.client.network;

import com.mechempire.client.constant.ServerConstant;
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

import javax.annotation.Resource;
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

    @Resource
    private GameClientHandler gameClientHandler;

    @Override
    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .remoteAddress(new InetSocketAddress(ServerConstant.host, ServerConstant.port));
            log.info("client try connect {}:{}", ServerConstant.host, ServerConstant.host);
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    log.info("client trigger connect.");
                    socketChannel.pipeline().addLast(gameClientHandler);
                }
            });

            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            if (channelFuture.isSuccess()) {
                log.info("client connect success!");
            }
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("connect server error: {}", e.getMessage(), e);
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}