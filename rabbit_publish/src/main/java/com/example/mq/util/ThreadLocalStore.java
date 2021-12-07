package com.study.mq.util;

/**
 * @Package: com.example.mq.util
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/05 15:37
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/05 15:37
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class ThreadLocalStore {
    private static final ThreadLocal<String> store = new ThreadLocal<>();

    public static void set(String element) {
        store.set(element);
    }

    public static void clear() {
        store.remove();
    }

    public static String get() {
        return store.get();
    }
}