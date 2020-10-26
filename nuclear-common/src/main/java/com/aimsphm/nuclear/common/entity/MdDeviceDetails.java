package com.aimsphm.nuclear.common.entity;

import lombok.Data;

/**
 * 设备详细信息
 *
 * @author MILLA
 * @since 2020-06-10
 */
@Data
public class MdDeviceDetails extends ModelBase {
    private static final long serialVersionUID = -3831271002426541741L;
    /**
     * 系统id
     */
    private Long systemId;
    /**
     * 子系统id
     */
    private Long subSystemId;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 字段名称-中文
     */
    private String fieldNameEn;
    /**
     * 字段名称-英文
     */
    private String fieldNameZh;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 字段单位
     */
    private String unit;
    /**
     * 重要程度-排序字段
     */
    private Integer importance;
}