package io.noi.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private static Selector selector;

    public static void main(String[] args) throws Exception {
        //1.客户端链接服务时,服务端会通过 ServerSocketChannel 得到 SocketChannel: 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2.切换为非阻塞模式
        ssChannel.configureBlocking(false);
        //3.绑定连接
        ssChannel.bind(new InetSocketAddress(3666));
        //4.获取选择器
        Selector selector = Selector.open();
        //5.将通道注册在选择器上,并指定监听"接收事件"
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端已启动:");
        //6.轮询选择器上已经准备就绪的事件
        int index =0;
        while (selector.select() >0){
            System.out.println("第"+(++index)+"次轮询");
            //7.获取当前选择器中所有注册的"选择键(已经就绪的监听事件)"
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                //8.获取已经准备就绪的事件
                SelectionKey sk = it.next();
                //9.判断具体是什么事件准备就绪
                if(sk.isAcceptable()){
                    //10.key -->"接收就绪事件",获取客户端链接
                    SocketChannel sChannel = ssChannel.accept();
                    //11.切换非阻塞模式
                    sChannel.configureBlocking(false);
                    //12.将该通道注册到选择器上
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    //13.获取当前选择器上"读就绪"状态的通道
                    SocketChannel sChannel =(SocketChannel) sk.channel();
                    //14.读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buf)) >0){
                        buf.flip();
                        String msg = new String(buf.array(), 0, len);
                        System.out.println(msg);
                        buf.clear();
                        if (msg.contains("off")){
                            sChannel.close();
                            break;
                        }
                        //if (msg.matches("/^off$/")){
                        //    sChannel.close();
                        //}
                    }
                }
            }
            //15.取消选择键SelectionKey
            it.remove();
        }


    }


}
