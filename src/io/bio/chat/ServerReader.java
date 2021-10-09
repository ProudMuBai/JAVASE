package io.bio.chat;

import cn.hutool.core.date.DateUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class ServerReader extends Thread{

    private Socket socket;

    public ServerReader(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dis =null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            /*1.循环等待客户端的消息*/
            while (true){
                /*读取当前消息类型: 登录/群发/私聊,@消息*/
                int flag = dis.readInt();
                System.out.println(flag);
                if (flag ==1){
                    /*将当前登录的客户端socket存到在线人数的socket集合中*/
                    String name = dis.readUTF();
                    System.out.println(name+"----->"+socket.getRemoteSocketAddress()); //getRemoteSocketAddress返回socket链接端点信息
                    ServerChat.onLineSockets.put(socket,name);

                }
                writeMsg(flag,dis);
            }
        }catch (Exception e){
            System.out.println("-----群成员:"+ServerChat.onLineSockets.get(socket)+"下线了-----");
            //从在线人数中将当前socket移除
            ServerChat.onLineSockets.remove(socket);
            try {
                //更新在线人数并发送给所有客户端
                writeMsg(1,dis);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void writeMsg(int flag,DataInputStream dis) throws Exception{
        //定义一个变量存放最终的消息形式
        String msg = null;
        if (flag == 1){
            /*读取所有在线人数发送给所有客户端去更新自己的在线人数列表*/
            /*onlineNames = [name1,name2,name3]*/
            StringBuilder rs = new StringBuilder();
            Collection<String> onlineNames = ServerChat.onLineSockets.values();
            //判断有成员在线时
            if (onlineNames !=null && onlineNames.size()>0){
                for (String name : onlineNames){
                    rs.append(name+Constants.SPLIT);
                    /*去掉尾部分隔符*/
                    msg = rs.substring(0,rs.lastIndexOf(Constants.SPLIT));
                    /*群发消息到所有客户端*/
                    sendMsgToAll(flag,msg);
                }
            }
        }else if(flag ==2 || flag ==3){
            //读取到的消息 2.群发 3.@消息
            String newMsg = dis.readUTF();
            //获取发件人
            String sendName = ServerChat.onLineSockets.get(socket);
            //内容
            StringBuilder msgFinal = new StringBuilder();
            //时间
            String sdf = "yyyy-MM-dd HH:mm:ss EEE";
            if (flag ==2){
                msgFinal.append(sendName).append("  ").append(DateUtil.now()).append("\r\n");
                msgFinal.append("    ").append(newMsg).append("\r\n");
                sendMsgToAll(flag,msgFinal.toString());
            } else if (flag == 3) {
                msgFinal.append(sendName).append("  ").append(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss EEE")).append("对您私发\r\n");
                msgFinal.append("    ").append(newMsg).append("\r\n");
                //得到私发的对象
                String destName = dis.readUTF();
                sendMsgToOne(destName,msgFinal.toString());
            }
        }
    }

    private void sendMsgToOne(String destName,String msg) throws Exception{
        //拿到所有的在线socket管道
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket s: allOnLineSockets){
            //得到当前需要私发的socket
            //只对对应name的socket私发消息
            if (ServerChat.onLineSockets.get(s).trim().equals(destName)){
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeInt(3);   //消息类型
                dos.writeUTF(msg);
                dos.flush();
            }
        }
    }



    private void sendMsgToAll(int flag,String msg) throws  Exception{
        /*
        *   1代表接收的是登陆消息
        *   2代表群发| @消息
        *   3代表了私聊消息
        */
        //拿到所有的socket管道,向这些管道发送消息
        Set<Socket> allOnLineSockets = ServerChat.onLineSockets.keySet();
        for (Socket s : allOnLineSockets){
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(flag);
            dos.writeUTF(msg);
            dos.flush();
        }
    }


}
