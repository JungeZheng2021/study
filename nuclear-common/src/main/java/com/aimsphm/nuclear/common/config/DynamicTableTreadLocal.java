package com.aimsphm.nuclear.common.config;

/**
 * @Package: com.study.auth.config.core
 * @Description: <动态表格存储类>
 * @Author: MILLA
 * @CreateDate: 2020/09/04 14:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/09/04 14:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum DynamicTableTreadLocal {
    INSTANCE;
    private ThreadLocal<String> tableName = new ThreadLocal<>();

    public String getTableName() {
        return tableName.get();
    }

    public void setTableName(String tableName) {
        this.tableName.set(tableName);
    }

    public void remove() {
        tableName.remove();
    }

}
