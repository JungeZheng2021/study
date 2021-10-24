package com.aimsphm.nuclear.common.config;

/**
 * <p>
 * 功能描述:动态表格存储类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
public enum DynamicTableTreadLocal {
    INSTANCE;
    private ThreadLocal<String> tableName = new ThreadLocal<>();

    public String getTableName() {
        return tableName.get();
    }

    public void tableName(String tableName) {
        this.tableName.set(tableName);
    }

    public void remove() {
        tableName.remove();
    }

}
