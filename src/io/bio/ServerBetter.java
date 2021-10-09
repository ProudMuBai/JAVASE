package io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerBetter {

    //默认端口号
    private static int DEFAULT_PORT = 3959;
    //单例Serversocket
    private static ServerSocket server;
    //懒汉式单例线程池
    private  static ExecutorService executorService =  Executors.newFixedThreadPool(60);


    public static void start() throws IOException {
        //使用默认端口号启动
        start(DEFAULT_PORT);
    }

    public synchronized static void start(int port) throws IOException {
        if (server !=null) return;

        try {
            server = new ServerSocket(port);
            System.out.println("服务器启动,端口号 : "+port);
            while (true){
                Socket socket = server.accept();

                executorService.execute(new ServerHandler(socket));
            }
        } finally {
            if (server != null){
                System.out.println("服务器关闭.");
                server.close();
                server = null;
            }
        }
    }


}
