import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;

import java.text.ParseException;
import java.util.Date;

public class test {

    public static void main(String[] args) {
        long time = 0;
        try {
            time = DateUtil.parseDateTime("2021-9-18 15:26:12").getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long l = DateUtil.current();
        System.out.println(time);
        System.out.println(l);

        String zodiac = DateUtil.getZodiac(Month.JANUARY.getValue(), 10);
        String chineseZodiac = DateUtil.getChineseZodiac(2000);
        System.out.println(zodiac);
        System.out.println(chineseZodiac);

        Date parse = DateUtil.parse("2021-11-18 15:26:12", "yyyy-MM-dd");
        DateTime date = DateUtil.date();
        System.out.println(parse);
        System.out.println(date);
        System.out.println(parse.compareTo(date));
        System.out.println(parse.getTime());

        long current = DateUtil.current();
        System.out.println(current > parse.getTime());


        System.out.println(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss EEE"));


    }
}
