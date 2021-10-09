package io.bio.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientReader extends Thread{

    private Socket socket;
    //接收客户端界面,方便收到消息后,更新界面数据
    private ClientChat clientChat;

    public ClientReader(ClientChat clientChat,Socket socket){
        this.clientChat = clientChat;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            /*循环等待客户端的消息*/
            while (true){
                /*读取当前的消息类型: 登录,群发,私聊,@消息*/
                int flag = dis.readInt();
                if (flag == 1){
                    //读取到在线人数消息
                    String nameDatas = dis.readUTF();
                    //展示到在线人数显示界面
                    String[] names = nameDatas.split(Constants.SPLIT);
                    clientChat.onLineUsers.setListData(names);
                }else {
                    //直接显示消息
                    String msg = dis.readUTF();
                    clientChat.smsContent.append(msg);
                    //让消息界面滚动到底端
                    clientChat.smsContent.setCaretPosition(clientChat.smsContent.getText().length());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
