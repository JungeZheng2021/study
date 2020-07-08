package com.milla.study.netbase.expert.concurrent;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/30 11:15
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/30 11:15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class VisibilityDemoTests {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
//        while (true) {
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//            new Thread(() -> T.write()).start();
//            new Thread(() -> T.read()).start();
//        }
    }

    static class T {
        final int x;
        int y;
        static T t;

        public T() {
            this.x = 3;
            this.y = 4;
        }

        public static void write() {
            t = new T();
        }

        public static void read() {
            if (t != null) {
                System.out.println("x:" + t.x);
                System.out.println("y:" + t.y);
                if (t.y == 0) {
                    throw new RuntimeException("找到0了");
                }
            }
        }

    }
}


