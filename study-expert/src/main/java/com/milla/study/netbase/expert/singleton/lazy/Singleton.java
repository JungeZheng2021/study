package com.milla.study.netbase.expert.singleton.lazy;

/**
 * @Description: <懒汉式>
 * @Author: milla
 * @CreateDate: 2020/09/18 10:33
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/18 10:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class Singleton {

    /**
     * 内部实例化
     */
    private static volatile Singleton instance;

    /**
     * 私有化构造
     */
    private Singleton() {
    }

    /**
     * 对外提供获取对象的方式
     *
     * @return
     */
    public static Singleton getInstance() {
        //如果对象已经被实例化就不用阻塞
        if (instance == null) {
            synchronized (Singleton.class) {
                //防止多个线程都判断实例对象为空，然后形成阻塞队列，重复实例对象
                if (instance == null) {
//                    JVM新建对象的时候，会经过三个步骤
//                    1.分配内存
//                    2.初始化构造器
//                    3.将对象指向分配的内存的地址
//                    PS:2和3可能会出现指令重排，导致重复创建对象，因此对象要要使用volatile关键字组织JVM指令重排
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}