package io.noi.channel;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {

    @Test
    public void write(){

        try {
            //1.创建字节输出流通向目标文件
            FileOutputStream fos = new FileOutputStream("data.txt");
            //2.得到字节输出流对应的通道channel
            FileChannel channel = fos.getChannel();
            //3.分配缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.put("这是用于测试channel write()方法写出数据的一段测试文字!".getBytes());
            //切换缓冲区为写出模式
            buf.flip();
            channel.write(buf);
            channel.close();
            System.out.println("输出写出完成!");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void read() throws Exception {
        //1.定义一个文件字节输入流与源文件连通
        FileInputStream fis = new FileInputStream("data.txt");
        //2.得到文件字节输入流的文件通道
        FileChannel channel = fis.getChannel();
        //3.定义缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //4.读取数据到缓冲区
        channel.read(buf);
        buf.flip();
        //5.读出缓冲区中数据并打印
        String s = new String(buf.array(),0,buf.remaining());
        System.out.println(s);
    }


    @Test
    public void copy() throws Exception{
        //源文件
        File secFile = new File("D:\\图片\\壁纸\\12.png");
        File destFile = new File("D:\\图片\\socket\\12.png");
        //得到字节输入流
        FileInputStream fis = new FileInputStream(secFile);
        //得到字节输出流
        FileOutputStream fos = new FileOutputStream(destFile);
        //得到对应的通道
        FileChannel isChannel = fis.getChannel();
        FileChannel osChannel = fos.getChannel();
        //分配缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int i = 0;
        while (true){
            System.out.println("文件已读写1024字节"+(++i)+"次");
            buf.clear();
            //开始读取一次数据
            int flag = isChannel.read(buf);
            if (flag == -1){
                //文件读取完成后跳出循环
                break;
            }
            //数据读取完成,将缓冲区切换成可读模式
            buf.flip();
            //将数据写出到目标文件
            osChannel.write(buf);
        }
    }

    @Test
    public void test() throws Exception{
        RandomAccessFile raf = new RandomAccessFile("data.txt", "rw");
        //1.获取通道
        FileChannel channel = raf.getChannel();

        //2.分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(6);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        //3.分散读取
        ByteBuffer[] bufs = {buf1,buf2};
        channel.read(bufs);

        for (ByteBuffer byteBuffer : bufs){
            byteBuffer.flip();
        }

        System.out.println(new String(bufs[0].array(),0,bufs[0].remaining()));
        System.out.println(new String(bufs[1].array(),0,bufs[1].remaining()));
        System.out.println("-----------读取完成------------");

        //4.聚焦写入
        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel raf2Channel = raf2.getChannel();

        raf2Channel.write(bufs);
    }

    @Test
    public void testTransferFrom() throws Exception{
        //1.字节输入流管道
        FileInputStream fis = new FileInputStream("data.txt");
        FileChannel fisChannel = fis.getChannel();
        //2.字节输出流管道
        FileOutputStream fos = new FileOutputStream("data02.txt");
        FileChannel fosChannel = fos.getChannel();
        //复制
        fosChannel.transferFrom(fisChannel,fisChannel.position(),fisChannel.size());
        fis.close();;
        fos.close();
    }

    @Test
    public void testTransferTo() throws IOException {
        //1.字节输入流管道
        FileInputStream fis = new FileInputStream("data.txt");
        FileChannel fisChannel = fis.getChannel();
        //2.字节输出流管道
        FileOutputStream fos = new FileOutputStream("data03.txt");
        FileChannel fosChannel = fos.getChannel();
        //复制
        fisChannel.transferTo(fisChannel.position(),fisChannel.size(),fosChannel);
        fis.close();
        fos.close();
    }

}
