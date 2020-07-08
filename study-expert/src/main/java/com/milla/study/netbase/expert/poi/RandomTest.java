package com.milla.study.netbase.expert.poi;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @Package: com.milla.study.netbase.expert.poi
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/19 22:23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/19 22:23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class RandomTest {
    public static void main(String[] args) throws InterruptedException {
        String s = "13579";
        char[] chars = s.toCharArray();
        List<Integer> integers = Lists.newArrayList();

        for (int i = 0; i < chars.length; i++) {
            String aChar = String.valueOf(chars[i]);
            integers.add(Integer.parseInt(aChar));
        }


        Random random = new Random();
        while (true) {
            int i = random.nextInt(integers.size());
            Integer integer = integers.get(i);
            System.out.println(integer);
            Thread.sleep(800L);
        }
    }
    /*
     *        0  1 2 3 4
     *  数组 ；[1,3,5,7,9]
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * */
}
