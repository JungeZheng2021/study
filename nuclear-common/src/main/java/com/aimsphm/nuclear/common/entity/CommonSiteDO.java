package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <电厂信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_site")
@ApiModel(value = "电厂信息实体")
public class CommonSiteDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -4812200700359074166L;

    @ApiModelProperty(value = "站点名称", notes = "")
    private String siteName;

    @ApiModelProperty(value = "站点描述", notes = "")
    private String siteDesc;

    @ApiModelProperty(value = "", notes = "")
    private Integer parentDistrict;

}