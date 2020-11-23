package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <导航结构树>
 * @Author: milla
 * @CreateDate: 2020/11/06 14:56
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/06 14:56
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class TreeVO<V, L> {
    /**
     * 真实id
     */
    private V value;
    /**
     * 展示名称
     */
    private L label;
    /**
     * 叶子节点
     */
    private List<TreeVO<V, L>> children;
    /**
     * 重要性-排序
     */
    private Integer importance;
    /**
     * 类别
     */
    private Integer category;

    public TreeVO(V value, L label) {
        this.value = value;
        this.label = label;
    }
}
