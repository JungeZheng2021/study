package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <系统信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_system")
@ApiModel(value = "系统信息实体")
public class CommonSystemDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5298085895046896175L;

    @ApiModelProperty(value = "站点id", notes = "")
    private Long setId;

    @ApiModelProperty(value = "系统名称", notes = "")
    private String systemName;

    @ApiModelProperty(value = "系统描述", notes = "")
    private String systemDesc;

}