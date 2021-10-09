package io.noi.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ServerChat {
    //定义属性
    private Selector selector;  //选择器
    private ServerSocketChannel ssChannel;      //socket通道
    private static  final int PORT = 3666;      //定义默认端口

    //创建构造器来初始化服务端
    public ServerChat(){
        try {
            //1.获取通道
            ssChannel = ServerSocketChannel.open();
            //2.切换为非阻塞方式
            ssChannel.configureBlocking(false);
            //3.绑定链接端口
            ssChannel.bind(new InetSocketAddress(PORT));
            //4.获取选择器Selector
            selector = Selector.open();
            //5.将通道都注册到选择器上去,并且指定监听ACCEPT接收事件
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听事件
    public void listen(){
        System.out.println("监听线程"+Thread.currentThread().getName());

        try {
            while (selector.select() >0){
                System.out.println("开始一轮事件处理~~~~");
                //6.获取选择器中的所有注册通道中已经就绪好的事件
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                //7.开始遍历迭代器中就绪的事件
                while (it.hasNext()){
                    //8.从集合中提取当前事件
                    SelectionKey sk = it.next();
                    //9.判断当前事件的具体类型
                    if (sk.isAcceptable()){
                        //10.直接获取当前接入的客户端
                        SocketChannel channel = ssChannel.accept();
                        //11.切换为非阻塞模式
                        channel.configureBlocking(false);
                        //12.将当前客户端通道注册到选择器
                        System.out.println(channel.getRemoteAddress().toString().substring(1).trim());
                        channel.register(selector,SelectionKey.OP_READ);

                    }else if(sk.isReadable()){
                        //实现读数据的私有方法
                        readData(sk);
                    }
                }
                it.remove(); //处理完成后移除当前事件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //异常处理
        }
    }

    //接收客户端的消息
    private void readData(SelectionKey sk){
        //取得关联客户端的channel
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) sk.channel();
            //创建Buffer缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int count;

            if ((count = channel.read(buf)) >0){
                //将缓冲区中的数据处理成字符串
                String msg = new String(buf.array());
                //输出消息
                System.out.println("from 客户端:"+channel.getRemoteAddress());
                //向除自己的客户端转发消息,使用群发的私有方法处理
                sendInfoToOtherClients(msg,channel);
            }
        }catch (Exception e){
            try {
                System.out.println(channel.getRemoteAddress()+"离线了");
                //e.printStackTrace();
                //取消注册
                sk.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //转发消息到其他客户端
    private void sendInfoToOtherClients(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
        //遍历所有注册到selector上的SocketChannel
        for (SelectionKey key : selector.keys()){
            //通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            //去除服务器本身
            if (targetChannel instanceof SocketChannel && targetChannel != self){
                //将targetChannel的类型转换为SocketChannel
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存放到buffer中
                ByteBuffer buf = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入channel中
                dest.write(buf);
            }
        }
    }


    public static void main(String[] args) {
        //创建服务器对象
        ServerChat serverChat = new ServerChat();
        serverChat.listen();
    }

}
