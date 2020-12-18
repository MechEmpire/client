package com.mechempire.client.network;

import com.mechempire.client.constant.ServerConstant;
import com.mechempire.client.network.handles.GameClientHandler;
import com.mechempire.client.network.handles.HeartBeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
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
    private final NioEventLoopGroup group = new NioEventLoopGroup();

    private SocketChannel socketChannel;

    @Override
    public void run() throws Exception {
        this.start();
    }

    public void start() {
        MechEmpireClient that = this;
        try {
            log.info("start to connect.");
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new IdleStateHandler(0, 0, 5)
                            );
                            GameClientHandler gameClientHandler = new GameClientHandler();
                            socketChannel.pipeline().addLast(gameClientHandler);
                            HeartBeatHandler heartBeatHandler = new HeartBeatHandler();
                            heartBeatHandler.setMechEmpireClient(that);
                            socketChannel.pipeline().addLast(heartBeatHandler);
                        }
                    });
            ChannelFuture channelFuture = clientBootstrap.connect(ServerConstant.host, ServerConstant.port);

            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("connect to {}:{} success", ServerConstant.host, ServerConstant.port);
                } else {
                    log.info("reconnect to {}:{}", ServerConstant.host, ServerConstant.port);
                    future.channel().eventLoop().schedule(this::start, 20, TimeUnit.SECONDS);
                }
            });

            socketChannel = (SocketChannel) channelFuture.channel();
            socketChannel.writeAndFlush(Unpooled.copiedBuffer("tairy", CharsetUtil.UTF_8));
        } catch (Exception e) {
            log.error("client start error: {}", e.getMessage(), e);
        }
    }
}