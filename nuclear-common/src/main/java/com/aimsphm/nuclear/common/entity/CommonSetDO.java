package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <机组信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_set")
@ApiModel(value = "机组信息实体")
public class CommonSetDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -9013186255625451652L;

    @ApiModelProperty(value = "站点id", notes = "")
    private Long siteId;

    @ApiModelProperty(value = "机组名称", notes = "")
    private String setName;

    @ApiModelProperty(value = "机组描述", notes = "")
    private String setDesc;

}