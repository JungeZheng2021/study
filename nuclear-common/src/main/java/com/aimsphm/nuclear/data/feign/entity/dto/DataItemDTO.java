package com.aimsphm.nuclear.data.feign.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 功能描述:服务入口
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/31 18:43
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataItemDTO implements Serializable {
    /**
     * 数据主键
     */
    private String itemId;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 数据值
     */
    private Object value;
    /**
     * 数据质量
     */
    private Short quality;
    /**
     * 数据时间
     */
    private Long timestamp;
    /**
     * 最近时刻
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date currentMoment;
}