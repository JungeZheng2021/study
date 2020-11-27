package com.milla.study.netbase.expert.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Package: com.milla.study.netbase.expert.netty.io
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/06 10:45
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/06 10:45
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws Exception {
        client();
    }

    private static void client() throws Exception {
        //创建客户端
        Socket client = new Socket();
        //连接服务端
        client.connect(new InetSocketAddress(InetAddress.getLocalHost(), 10010));
        //异步发送信息
        Thread send = new Thread(() -> sendMessage(client), "send");
        send.start();
        //接收信息
        getMessage(client);

    }

    private static void getMessage(Socket client) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String line;
            while (!client.isClosed() && (line = reader.readLine()) != null) {
                log.info("聊天数据：{}", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(Socket client) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            String requestLine = null;
            while (!"quit".equals(requestLine)) {
                requestLine = scanner.nextLine();
                writer.write(requestLine);
                writer.newLine();
                writer.flush();
            }
            log.info("当前线程名称： {}", Thread.currentThread().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
