package io.bio;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNormal {

    private static int DEFAULT_PORT = 3959;
    private static ServerSocket server;

    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public synchronized static void  start(int port) throws IOException {
        if(server !=null) return;
        try {
            server = new ServerSocket(port);
            System.out.println("服务端启动,端口号 :"+port);
            while (true){
                Socket socket = server.accept();
                /**
                 * 有新客户端接入时,执行下发代码,生成新线程处理socket链路
                 */
                new Thread(new ServerHandler(socket)).start();
            }
        } finally {
            if (server !=null){
                System.out.println("服务器关闭");
                server.close();
                server = null;
            }
        }
    }
}
