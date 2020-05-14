package com.milla.study.netbase.expert.quartz.enums;

/**
 * @Package: com.milla.study.netbase.expert.quartz.enums
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum QuartzJobStateEnum {


    STOPPED("0", "停止"),

    RUNNING("1", "运行"),

    DELETED("-1", "删除");


    private String value;
    private String code;

    private QuartzJobStateEnum(String code, String value) {
        this.value = value;
        this.code = code;
    }

    public static QuartzJobStateEnum instance(String code) {
        for (QuartzJobStateEnum e : QuartzJobStateEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取value
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取code
     */
    public String getCode() {
        return code;
    }
}

