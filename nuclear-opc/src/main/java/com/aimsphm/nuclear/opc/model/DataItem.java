package com.aimsphm.nuclear.opc.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * @author Administrator
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DataItem implements Serializable {

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
