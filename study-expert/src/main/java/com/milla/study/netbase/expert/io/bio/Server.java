package com.milla.study.netbase.expert.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.milla.study.netbase.expert.netty.io
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/06 10:39
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/06 10:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws Exception {
        server();
    }

    static final Map<Socket, Object> clients = new ConcurrentHashMap<>(16);

    private static void server() throws Exception {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 30, 5000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(3), (a, b) -> System.out.println("自定义"));

        //定义服务端口
        ServerSocket server = new ServerSocket(10010);
        log.info("服务端启动中......");
        log.info("服务端已启动......");
        //接收客户端连接
        while (!server.isClosed()) {
            Socket socket = server.accept();
            pool.execute(() -> test(socket));

        }
        server.close();
    }

    private static void test(Socket socket) {
        try {
            clients.putIfAbsent(socket, socket);
            log.info("客户端: {} 已经接入.....", socket);
            //从客户端获取输入流

            //包装输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while (!socket.isInputShutdown() && (line = reader.readLine()) != null) {
                if ("quit".equals(line)) {
                    log.info("客户端： {}，退出连接", socket);
                    clients.remove(socket);
//                    socket.shutdownInput();
//                    socket.shutdownOutput();
                    break;
                }
                senderMessages2Clients(line, socket);
                log.info("客户端：{} 数据为：{}", socket, line);
            }
        } catch (IOException e) {
            log.info("server exception");
        }
    }

    private static void senderMessages2Clients(String line, Socket socket) {
        if (clients.isEmpty()) {
            return;
        }
        Set<Map.Entry<Socket, Object>> entries = clients.entrySet();

        entries.stream().filter(item -> !item.getKey().isClosed()).forEach((item -> {
            Socket client = item.getKey();
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                writer.write(socket.getPort() + " - " + line);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }
}
