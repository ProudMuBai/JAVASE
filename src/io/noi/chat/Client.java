package io.noi.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class Client {
    //定义相关的属性
    private final String HOST = "127.0.0.1";    //服务器IP
    private final int PORT = 3666;              //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //构造器,完成初始化工作
    public Client() throws IOException {

        selector = Selector.open();
        //链接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        System.out.println(socketChannel.getLocalAddress());
        username = socketChannel.getLocalAddress().toString().substring(1).trim();
        System.out.println(username+ "is_ok...");
    }

    //向服务器发送消息
    public void sendInfo(String info){
        info = username + ":"+info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void readInfo(){
        try {
            int readChannel = selector.select();
            if(readChannel >0){ //存在可用通道时
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()){
                    //得到读事件key
                    SelectionKey key = it.next();
                    if (key.isReadable()){
                        //其获取对应的通道
                        SocketChannel  channel = (SocketChannel) key.channel();
                        //创建缓冲区
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        //读取数据
                        channel.read(buf);
                        //将缓冲区数据转换成字符串
                        String msg = new String(buf.array());
                        System.out.println(msg.trim());
                    }
                    it.remove();    //处理完成之后删除selectionKey,防止重复触发
                }
            }else{
                //System.out.println("当前无可用通道!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception{
        //启动客户端
        Client client = new Client();
        //启动一个线程用于读取服务器发送的消息/每隔3s触发一次
        new Thread(()->{
            while (true){
                client.readInfo();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //发送数据给客户端
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String msg = sc.nextLine();
            client.sendInfo(msg);
        }

    }







}
