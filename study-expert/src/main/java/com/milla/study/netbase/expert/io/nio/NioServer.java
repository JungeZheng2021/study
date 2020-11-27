package com.milla.study.netbase.expert.io.nio;

import com.google.code.yanf4j.util.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Package: com.milla.study.netbase.expert.netty.nio.server
 * @Description: <NIO模式下的Server>
 * @Author: milla
 * @CreateDate: 2020/08/07 10:32
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/07 10:32
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class NioServer {
    /**
     * 消息缓存区
     */
    private static ByteBuffer readBuf = ByteBuffer.allocate(1024);
    /**
     * 保存的客户端
     */
    static Set<SocketChannel> clients = new ConcurrentHashSet<>();
    /**
     * 选择器
     */
    private static Selector selector;

    public static void main(String[] args) throws Exception {

        server();
    }

    private static void server() throws Exception {
        //获取一个channel
        ServerSocketChannel channel = ServerSocketChannel.open();
        //配置是否阻塞
        channel.configureBlocking(false);
        //获取socket
        ServerSocket server = channel.socket();
        //绑定服务及端口
        server.bind(new InetSocketAddress(InetAddress.getLocalHost(), 10010));
        //获取到选择器
        selector = Selector.open();
        //注册到选择器用以接受连接
        channel.register(selector, SelectionKey.OP_ACCEPT);

        log.info("server is start....");
        //一直能处理
        while (true) {
            //等待需要处理的新事件，阻塞将一直持续到下一个传入事件
            selector.select();
            //获取所有接收事件的selection-key实例
            Set<SelectionKey> readKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = readKeys.iterator();

            while (iterator.hasNext()) {
                try {
                    SelectionKey key = iterator.next();
                    //检查时间是否是一个新的已经就绪可以被接受的连接
                    if (key.isAcceptable()) {
                        getConnect(key, selector);
                    }
                    //是否可以读取数据
                    if (key.isReadable()) {
                        getMessage(key);
                    }
                    //检查套接字是否已经准备写数据
//                    if (key.isWritable()) {
                    //写操作
//                    }
                    //处理完成之后要将key删除防止重复处理
                    iterator.remove();
                } catch (Exception e) {
                    log.info("错误：{}", e);
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 获取连接
     *
     * @param key
     * @param selector
     * @throws Exception
     */
    private static void getConnect(SelectionKey key, Selector selector) throws Exception {
        //要操作的message
        ByteBuffer msg = ByteBuffer.wrap("connect success ".getBytes());
        ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
        //接收一个连接
        SocketChannel client = serverSocket.accept();
        clients.add(client);
        //设置非阻塞
        client.configureBlocking(false);
        //监听读/写事件
        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        //发送连接成功给客户端
        client.write(msg);
        log.info(" a new socket connected...{}", client);
    }

    /**
     * 读取数据
     *
     * @param key
     * @throws IOException
     */
    private static void getMessage(SelectionKey key) throws IOException {
        readBuf.clear();
        SocketChannel client = (SocketChannel) key.channel();
        try {
            int count = client.read(readBuf);
            if (count == -1) {
                client.shutdownOutput();
                client.shutdownInput();
                client.close();
                log.info("断开连接....");
                clients.remove(key);
            }
            byte[] bytes = new byte[count];
            readBuf.flip();
            readBuf.get(bytes);
            String message = new String(bytes, 0, count);
            log.info("接收到信息：{}", message);
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            sendMessages2Clients(message);
        } catch (IOException e) {
            key.cancel();
            client.close();
            log.error("端开连接");
            clients.remove(key);
        }
    }

    /**
     * 接收到数据将数据群发到各个客户端
     *
     * @param message
     */
    private static void sendMessages2Clients(String message) {
        clients.stream().forEach(client -> {
            try {
                client.write(ByteBuffer.wrap(message.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
