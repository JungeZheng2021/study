package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;
import org.apache.ibatis.annotations.ConstructorArgs;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <统计展示实体>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 10:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 10:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class LabelVO {
    /**
     * 名称
     */
    private Object name;
    /**
     * 占比
     */
    private Object value;

    public LabelVO(Object name, Object value) {
        this.name = name;
        this.value = value;
    }

    public LabelVO() {
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
