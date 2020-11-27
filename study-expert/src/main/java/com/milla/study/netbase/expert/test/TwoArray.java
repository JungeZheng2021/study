package com.milla.study.netbase.expert.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @Package: com.milla.study.netbase.expert.test
 * @Description: <在一个二维数组中，每一行都按照从左到右的顺序排序，每一列都按照从上到下的顺序排序，完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否包含该整数及位置>
 * @Author: milla
 * @CreateDate: 2020/09/18 13:33
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/18 13:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class TwoArray {
    public static void main(String[] args) {
        int[][] a = {
                {1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8},
                {4, 5, 6, 7, 8, 9},
                {5, 6, 7, 8, 9, 10},
                {6, 7, 8, 9, 10, 11},
                {7, 8, 9, 10, 11, 12}
        };
        int test = arrayOne(a[0], 7);
        int demo = arrayTwo(a, 9);
        log.info("一维数组查找指定数字：{}", test);
        log.info("二维数组查找指定数字：{}", demo);
    }

    /**
     * 二维数组获取某个要查询的值
     *
     * @param array  二维数组
     * @param search 要查找的数字
     * @return
     */
    private static int arrayTwo(int[][] array, int search) {

        int count = arrayOne(array[0], search);
        if (count < 0) {
            count = Math.abs(count) - 2;
        }
        //因为数组是有序的，所以只需要从第一个位置找到出现数字的位置即可
        for (int j = 0; j <= count; j++) {
            int rowStart = 0;
            int rowEnd = array.length - 1;

            while (rowStart <= rowEnd) {
                int midIndex = (rowStart + rowEnd) >> 1;
                int mid = array[midIndex][j];
                if (mid < search) {
                    rowStart = midIndex + 1;
                } else if (mid > search) {
                    rowEnd = midIndex - 1;
                } else {
                    log.info("找到的行索引为：{}, 列索引为：{}, 值为:{} ", midIndex, j, mid);
                    break;
                }
            }
        }
        return 0;
    }

    /**
     * 一维数组二分法查找数据
     *
     * @param one    一维数组
     * @param search 要查询的数字
     * @return 如果存在就返回数字真实的索引值，如果不存在返回该数字应该出现的索引值
     */
    private static int arrayOne(int[] one, int search) {
        int start = 0;
        int end = one.length - 1;

        while (start <= end) {
            int half = (start + end) << 1;
            log.info("开始：{},中部:{},结束：{}", start, half, end);
            int middle = one[half];
            if (middle < search) {
                start = half + 1;
            } else if (middle > search) {
                end = half - 1;
            } else {
                return half;
            }
        }
        return -(start + 1);
    }
}
