package com.milla.study.netbase.expert.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @Package: com.milla.study.netbase.expert.io.nio
 * @Description: <NIO下的client>
 * @Author: milla
 * @CreateDate: 2020/08/07 11:16
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/07 11:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class NioClient {

    /**
     * 退出聊天的标识
     */
    private static volatile boolean connected = true;
    /**
     * 输入流
     */
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        init();
        new Thread(() -> write()).start();
        new Thread(() -> read()).start();

    }

    /**
     * 写数据线程
     *
     * @throws IOException
     */
    private static void write() {

        while (connected) {
            try {
                //获取选择器
                selector.select();
                //获取选择器的已选择键集
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    //处理完当前的key需要删除，防止重复处理
                    it.remove();
                    if (key.isConnectable()) {
                        log.info("try connecting .... ");
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);
                        channel.finishConnect();
                    }
                    //数据是否可写
                    if (key.isWritable()) {
                        sendMessage(key);
                    }
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static Selector selector;

    /**
     * 初始化连接服务器
     *
     * @throws IOException
     */
    private static void init() throws IOException {
        //打开socket通道
        SocketChannel sc = SocketChannel.open();
        //设置非阻塞
        sc.configureBlocking(false);
        //连接到服务器-指定主机名称和端口
        sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 10010));
        //打开选择器
        selector = Selector.open();
        //注册到服务器上的socket动作
        sc.register(selector, SelectionKey.OP_CONNECT);
    }

    /**
     * 读数据线程
     *
     * @throws IOException
     */
    private static void read() {

        while (connected) {
            try {
                //获取选择器
                selector.select();

                //获取选择器的已选择键集
                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                Iterator<SelectionKey> it = selectedKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    //处理完当前的key需要删除，防止重复处理
                    it.remove();
                    if (key.isConnectable()) {
                        log.info("try connecting .... ");
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);
                        //完成连接
                        channel.finishConnect();

                        //下面这种方式也可以
                        //sc.finishConnect();
                        //sc.register(selector, SelectionKey.OP_WRITE);
                    }
                    //数据是否可读
                    if (key.isReadable()) {
                        getMessage(key);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送数据
     *
     * @param key
     * @throws IOException
     */
    private static void sendMessage(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        try {
            String requestLine = scanner.nextLine();
            //写数据
            channel.write(ByteBuffer.wrap(requestLine.getBytes()));
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            //设置标识符退出聊天
            if ("quit".equals(requestLine)) {
                connected = false;
                log.info("退出聊天...", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据
     *
     * @param key
     * @throws IOException
     */
    private static void getMessage(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //清空缓存数据
        buffer.clear();
        //读取数据
        int count = channel.read(buffer);
        byte[] bytes = new byte[count];
        //弹出数据
        buffer.flip();
        buffer.get(bytes);
        log.info("接收到服务器消息：{}", new String(bytes));
    }
}