package com.study.auth.config.core.observer;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Vector;

/**
 * @Package: com.study.auth.config.core.observer
 * @Description: 主题（发布者、被观察者）
 * @Author: milla
 * @CreateDate: 2020/09/09 16:26
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/09 16:26
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public abstract class Subject {
    protected List<LoginObserver> loginObservers = new Vector<>();

    /**
     * 注册观察者
     */
    void registerObserver(LoginObserver loginObserver) {
        loginObservers.add(loginObserver);
    }

    /**
     * 移除观察者
     */
    void removeObserver(LoginObserver loginObserver) {
        loginObservers.remove(loginObserver);
    }

    /**
     * 通知观察者
     * @return
     */
    abstract UserDetails notifyObservers();
}
