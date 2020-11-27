package com.milla.study.stream;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @Package: com.milla.study.stream
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/23 18:00
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/23 18:00
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Map<String, Object> map = Maps.newHashMap();
        Set<String> strings = map.keySet();
        strings.add("Q");

    }
}
