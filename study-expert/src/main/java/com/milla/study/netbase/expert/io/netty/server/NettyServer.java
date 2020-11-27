package com.milla.study.netbase.expert.io.netty.server;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @Package: com.milla.study.netbase.expert.io.netty.oio
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/10 15:27
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/10 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class NettyServer {
    public static void main(String[] args) throws Exception {
        oioServer();
//        nioServer();
    }

    private static void nioServer() throws Exception {
        ByteBuf buffer = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("I am the message", Charsets.UTF_8));

        NioEventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap();
        server.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(InetAddress.getLocalHost(), 10010))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(buffer.duplicate());
                                super.channelActive(ctx);
                            }
                        });
                    }
                });
        ChannelFuture future = server.bind().sync();
        future.channel().closeFuture().sync();

    }

    private static void oioServer() throws Exception {
        ByteBuf buffer = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("I am the message", Charsets.UTF_8));
        OioEventLoopGroup group = new OioEventLoopGroup();

        ServerBootstrap server = new ServerBootstrap();
        server.group(group).channel(OioServerSocketChannel.class).localAddress(new InetSocketAddress(InetAddress.getLocalHost(), 10010))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ctx.writeAndFlush(buffer.duplicate());
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                String message = ((AbstractReferenceCountedByteBuf) msg).toString(CharsetUtil.UTF_8);
                                log.info("接收到的数据：{}", message);
                                ctx.writeAndFlush(message);
                            }
                        });
                    }
                });
        ChannelFuture future = server.bind().sync();
        future.channel().closeFuture().sync();

    }
}


