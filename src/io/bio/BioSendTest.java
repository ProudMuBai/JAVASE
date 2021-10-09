package io.bio;

import java.io.IOException;
import java.util.Random;

public class BioSendTest {

    public static void main(String[] args) throws InterruptedException {

        //启动服务端
        new  Thread( ()->{
            try {
//                ServerNormal.start();|
                ServerBetter.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //避免客户端比服务端先启动
        Thread.sleep(100);

        //启动客户单
        char str[]={'+','-','*','/'};
        Random random = new Random(System.currentTimeMillis());
        new Thread(()->{
            while (true){
                //随机产生算术表达式
                String expression = random.nextInt(10)+""+str[random.nextInt(4)]+(random.nextInt(10)+1);
                Client01.send(expression);
                try {
                    Thread.currentThread().sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
