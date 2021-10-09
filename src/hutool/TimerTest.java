package hutool;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;

import java.util.Date;

public class TimerTest {
    public static void main(String[] args) {

        DateTime dateTime = DateUtil.parseDateTime("2021-9-18 17:00:00");
//        DateUtil.date().compareTo(dateTime) !=1
        int i=0;
        TimeInterval timer = DateUtil.timer();
        System.out.println(timer.start());
        long oldDate = 0;
        while (DateUtil.date().compareTo(dateTime) !=1){
            long newDate = timer.intervalSecond();
            if(oldDate < newDate){
                System.out.println("距离"+timer.toString()+"已过去"+newDate+"秒!");//花费毫秒数
            }
            oldDate = newDate;
//            System.out.println(timer.intervalRestart()+"xxxxxxxxxxxx");//返回花费时间，并重置开始时间
//            System.out.println(timer.intervalMinute());//花费分钟数
        }

    }
}
