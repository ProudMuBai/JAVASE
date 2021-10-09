package io.bio;

import cn.hutool.core.lang.UUID;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServerReaderThread extends Thread{

    private Socket socket;
    public ServerReaderThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1.得到一个数据输入流读取客户端发送的数据
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            //2.读取客户端发送的文件类型
            String suffix = dis.readUTF();
            System.out.println("接收到的文件后缀为:"+suffix);
            //3.定义一个字节输出管道负责包客户端发送的文件数据输出
            OutputStream os = new FileOutputStream("D:\\图片\\socket\\"+ UUID.randomUUID().toString()+suffix);
            //4.从数据输入流中读取文件数据表,写出到字节输出流中
            byte[] buffer = new byte[1024];
            int len;
            while ((len =dis.read(buffer)) >0){
                os.write(buffer,0,len);
            }
            os.close();
            System.out.println("服务端单个文件接收完毕!");
            dis.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
