package com.example.order.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 功能描述:基础实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-12-07
 */
@ApiModel(value = "基础实体")
@Data
public abstract class BaseDO implements Serializable {

    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = 1L;
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段", notes = "默认按照记录id正序输出")
    @TableField(fill = FieldFill.INSERT_UPDATE, update = "12")
    private Integer importance;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", notes = "")
    private String creator;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", notes = "")
    private String modifier;
    /**
     * 数据创建时间
     */
    @ApiModelProperty(value = "数据创建时间", notes = "")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;
    /**
     * 数据被修改时间
     */
    @ApiModelProperty(value = "数据被修改时间", notes = "")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableLogic
    /**
     * 数据是否被删除
     */
    @ApiModelProperty(value = "数据是否被删除", notes = "逻辑删除字段")
    protected Integer isDeleted;

    @TableId(value = "id", type = IdType.AUTO)
    /**
     *主键自增
     */
    @ApiModelProperty(value = "主键自增", notes = "")
    protected Long id;
}
