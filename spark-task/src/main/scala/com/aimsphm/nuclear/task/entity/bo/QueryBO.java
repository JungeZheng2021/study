package com.aimsphm.nuclear.task.entity.bo;

/**
 * @Package: com.aimsphm.nuclear.task.entity.bo
 * @Description: <查询实体>
 * @Author: MILLA
 * @CreateDate: 2020/12/10 16:26
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/10 16:26
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class QueryBO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8553983655858153891L;
    /**
     * 默认的列族
     */
    public final static String DEFAULT_COLUMN_FAMILY = "pRaw";
    /**
     * 开始时间
     */
    private String start;
    /**
     * 结束时间
     */
    private String end;
    /**
     * 表格名称
     */
    private String tableName;
    /**
     * 默认的列族名称
     */
    private String defaultFamily;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDefaultFamily() {
        return defaultFamily;
    }

    public void setDefaultFamily(String defaultFamily) {
        this.defaultFamily = defaultFamily;
    }

    public QueryBO(String start, String end, String tableName, String defaultFamily) {
        this.start = start;
        this.end = end;
        this.tableName = tableName;
        this.defaultFamily = defaultFamily;
    }

    public QueryBO(String start, String end, String tableName) {
        this.start = start;
        this.end = end;
        this.tableName = tableName;
        this.defaultFamily = DEFAULT_COLUMN_FAMILY;
    }

    @Override
    public String toString() {
        return "QueryBO{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", tableName='" + tableName + '\'' +
                ", defaultFamily='" + defaultFamily + '\'' +
                '}';
    }
}
