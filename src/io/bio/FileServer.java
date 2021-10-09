package io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(3800);
            System.out.println("Server服务器已启动!");
            while (true){
                Socket socket = server.accept();
                //开启新线程处理客户端文件通信需求
                new ServerReaderThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
