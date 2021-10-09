package test;

import java.math.BigDecimal;

public class EqTest1 {
    public static void main(String[] args) {
        Long num1 = 0L;
        Integer num2 = 0;
        BigDecimal num3 = BigDecimal.valueOf(0);

        long nums1 = 0L;
        int nums2 =0;

        System.out.println(num1.equals(num2));
        System.out.println(num1.equals(num3));
        System.out.println(num2.equals(num3));
        System.out.println(nums1==nums2);
        System.out.println(num3.equals(nums1));
        System.out.println(num3.equals(nums2));


        for (int i = 1; i <= 20; i++) {
            System.out.println(i+"."+i+"."+i+"."+i);
        }
    }
}
