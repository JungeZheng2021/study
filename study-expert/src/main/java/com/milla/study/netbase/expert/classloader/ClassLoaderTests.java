package com.milla.study.netbase.expert.classloader;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * @Package: com.milla.study.netbase.expert.classloader
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/7/6 18:12
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/7/6 18:12
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class ClassLoaderTests {
    public static void main(String[] args) throws Exception {
        testClassLoader();
        testClassLoading();
    }

    private static void testClassLoading() throws Exception {
        URL url = new URL("file:C:\\java\\");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        while (true) {
            if (urlClassLoader == null) {
                break;
            }
            Class<?> aClass = urlClassLoader.loadClass("HelloLoaderService");
            Object service = aClass.newInstance();
            Method get = aClass.getMethod("get", null);
            Object invoke = get.invoke(service, null);
            System.out.println(invoke);
            Thread.sleep(3000L);

            service = null;
            urlClassLoader = null;
        }
        System.gc();
        Thread.sleep(10000L);
    }

    private static void testClassLoader() throws ClassNotFoundException {
        ClassLoader classLoader = ClassLoaderTests.class.getClassLoader();
        Class<?> aClass = classLoader.loadClass("java.lang.String");
        ClassLoader classLoader1 = aClass.getClassLoader();

        log.info("核心类库加载器：{}", ClassLoaderTests.class.getClassLoader().loadClass("java.lang.String").getClassLoader());
        log.info("拓展类库加载器：{}", ClassLoaderTests.class.getClassLoader().loadClass("com.sun.nio.zipfs.ZipCoder").getClassLoader());
        log.info("应用程序类库加载器：{}", ClassLoaderTests.class.getClassLoader());
        log.info("应用程序类库加载器的父类：{}", ClassLoaderTests.class.getClassLoader().getParent());
        log.info("应用程序类库加载器的父类的父类：{}", ClassLoaderTests.class.getClassLoader().getParent().getParent());

    }
}