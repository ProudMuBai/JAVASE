package io.bio;


import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileClicen {

    public static void main(String[] args) {
        for (int i = 7; i <= 10; i++) {
            try(InputStream is = new FileInputStream("D:\\图片\\壁纸\\"+i+".jpg");){
                //1.请求socket链接
                Socket socket = new Socket("127.0.0.1", 3800);
                System.out.println("客户端已启动!");
                //2.包装字节输出流为数据输出流
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                //3.将文件后缀先发送给服务端
                dos.writeUTF(".jpg");
                //4.文件发送给服务端进行接收
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer))>0){
                    dos.write(buffer,0,len);
                }
                dos.flush();
                Thread.sleep(3000);
                if (i==10){
                    dos.writeChars("close");
                    dos.flush();
                }
                dos.close();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }
}
