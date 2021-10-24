package com.aimsphm.nuclear.algorithm.util;

/**
 * <p>
 * 功能描述:原始数据本地线程存储
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
public enum RawDataThreadLocal {
    INSTANCE;
    private ThreadLocal<Boolean> whether = new ThreadLocal<>();

    public Boolean getWhether() {
        return whether.get();
    }

    public void perhaps(Boolean yes) {
        this.whether.set(yes);
    }

    public void remove() {
        whether.remove();
    }

}
