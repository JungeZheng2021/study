package com.aimsphm.nuclear.report.util;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * @Package: com.aimsphm.nuclear.report.util
 * @Description: <获取唯一标识>
 * @Author: MILLA
 * @CreateDate: 2020/5/11 10:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/11 10:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public final class UUIDUtils {

    /**
     * 获取唯一标识
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        String s = "测点%s趋势%s，变化强度 %s";
        log.debug(String.format(s, "XX", "下降", 120));
    }

}
