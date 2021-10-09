package io.bio.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ServerChat {

    /*定义集合存放所有在线的socket*/
    public static Map<Socket,String> onLineSockets = new HashMap<>();

    public static void main(String[] args) {

        try {
            /*1.注册端口*/
            ServerSocket serverSocket = new ServerSocket(Constants.PORT);
            /*循环等待客户端的链接*/
            while (true){
                Socket socket = serverSocket.accept();
                /*单独开启线程来配置客户端socket管道*/
                new ServerReader(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
