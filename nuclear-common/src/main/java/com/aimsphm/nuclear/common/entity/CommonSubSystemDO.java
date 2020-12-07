package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <子系统信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_sub_system")
@ApiModel(value = "子系统信息实体")
public class CommonSubSystemDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5813587557967430967L;

    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "子系统名称", notes = "")
    private String subSystemName;

    @ApiModelProperty(value = "描述", notes = "")
    private String subSystemDesc;

    @ApiModelProperty(value = "类型", notes = "")
    private Integer subSystemType;

    @ApiModelProperty(value = "", notes = "")
    private Integer additionalType;

    @ApiModelProperty(value = "", notes = "")
    private Long forecastAlgoId;

    @ApiModelProperty(value = "", notes = "")
    private Long trendfAlgoId;

    @ApiModelProperty(value = "", notes = "")
    private Long trendrAlgoId;

    @ApiModelProperty(value = "", notes = "")
    private Long faultdAlgoId;

    @ApiModelProperty(value = "", notes = "")
    private Long vifAlgoId;

}