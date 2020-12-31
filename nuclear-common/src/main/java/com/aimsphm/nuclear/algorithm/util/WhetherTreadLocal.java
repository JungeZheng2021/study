package com.aimsphm.nuclear.algorithm.util;

/**
 * @Package: com.study.auth.config.core
 * @Description: <是否逻辑线程存储>
 * @Author: MILLA
 * @CreateDate: 2020/09/04 14:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/09/04 14:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum WhetherTreadLocal {
    INSTANCE;
    private ThreadLocal<Boolean> whether = new ThreadLocal<>();

    public Boolean getWhether() {
        return whether.get();
    }

    public void setWhether(Boolean yes) {
        this.whether.set(yes);
    }

    public void remove() {
        whether.remove();
    }

}
