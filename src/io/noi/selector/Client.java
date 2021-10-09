package io.noi.selector;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        //1.获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 3666));
        System.out.println("客户端已启动:");
        //2.切换为非阻塞模式
        sChannel.configureBlocking(false);
        //3.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //4.发送数据给服务端
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String s = sc.nextLine();
            buf.put((DateUtil.now() +"\n" +s).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            if (s.equals("off")){
                //sChannel.shutdownOutput();
                break;
            }
        }
        //关闭通道
        sc.close();
        sChannel.close();
    }
}
