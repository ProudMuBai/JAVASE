package io.noi.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {
        BufferTest bt = new BufferTest();
        bt.test();
    }




    @Test
    public void test(){
        //创建容量为1024的缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        //判断是否为直接缓冲区
        System.out.println(buf.isDirect());
    }

    @Test
    public void test2(){
        String str = "buffer test !";
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();

        byte[] dst = new byte[buf.limit()];                             //buf.limit() -->返回buf缓冲区的界限
        buf.get(dst,0,2);
        System.out.println(new String(dst,0,2)+"xxxxxxxx"); //bu
        System.out.println(buf.position());                              //当前位置的索引

        //mark(): 标记
        buf.mark();                                                     //标记缓冲区的当前位置

        buf.get(dst,2,2);
        System.out.println(new String(dst,2,2));           //ff
        System.out.println(buf.position());                            //4

        //reset() : 回复到mark的位置
        buf.reset();                                                   //将位置 position 转到以前设置的mark
        System.out.println(buf.position());                            //2

        //判断缓冲区中是否还有剩余数据
        if (buf.hasRemaining()){
            //获取缓冲区中可以操作的数量
            System.out.println(buf.remaining());
        }
        buf.rewind();
        System.out.println(buf.remaining());                           //将位置设为为 0， 取消设置的 mark

    }

    @Test
    public void Test(){
        //1.分配一个指定容量的非直接缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        System.out.println("-----------------allocate()--------------------"+buf.isDirect());
        System.out.println(buf.position());             //返回当前位置         0
        System.out.println(buf.limit());                //返回缓冲区的界限      1024
        System.out.println(buf.capacity());             //返回缓冲区的容量      1024


        //2.使用put()向缓冲区中存放数据
        String str = "buffer put()!";
        buf.put(str.getBytes());
        System.out.println("-------------------put()------------------");
        System.out.println(buf.position());             //返回当前位置            13
        System.out.println(buf.limit());                //返回缓冲区的界限         1024
        System.out.println(buf.capacity());             //返回缓冲区的容量         1024


        //3.切换为读数据模式
        buf.flip();
        System.out.println("-------------------flip()------------------");
        System.out.println(buf.position());             //返回当前位置            0
        System.out.println(buf.limit());                //返回缓冲区的界限         13
        System.out.println(buf.capacity());             //返回缓冲区的容量         1024


        //4.使用get读取缓冲区中的数据
        System.out.println("-------------------get()------------------");
        byte[] dst = new byte[buf.limit()];
        //dst = new byte[1024];
        buf.get(dst);       //如果读取长度大于buf设置的界限就会抛出java.nio.BufferUnderflowException异常
        System.out.println(new String(dst,0,dst.length));               //buffer put()!
        System.out.println(buf.position());             //返回当前位置            13
        System.out.println(buf.limit());                //返回缓冲区的界限         13
        System.out.println(buf.capacity());             //返回缓冲区的容量         1024


        //5.rewind() :将位置设为为 0， 取消设置的 mark -->重复读
        System.out.println("-------------------rewind()------------------");
        buf.rewind();
        System.out.println(buf.position());             //返回当前位置            0
        System.out.println(buf.limit());                //返回缓冲区的界限         13
        System.out.println(buf.capacity());             //返回缓冲区的容量         1024


        //6.clear() : 清空缓冲区,但缓冲区中的数据依然存在,再次写入时
        System.out.println("-------------------clear()------------------");
        buf.clear();
        System.out.println(buf.position());             //返回当前位置            0
        System.out.println(buf.limit());                //返回缓冲区的界限         1024
        System.out.println(buf.capacity());             //返回缓冲区的容量         1024
        System.out.println((char)buf.get());            //clear()后缓冲区的内容依旧存在


    }

    
}
