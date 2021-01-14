package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <振动分析收藏夹实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("analysis_favorite")
@ApiModel(value = "振动分析收藏夹实体")
public class AnalysisFavoriteDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -4655554544225620445L;

    @ApiModelProperty(value = "测点编号", notes = "")
    private String pointId;

    @ApiModelProperty(value = "数据采集时间", notes = "")
    private Long acquisitionTime;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "所属设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "所属设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "分析种类", notes = "1：振动分析")
    private Integer category;

    @ApiModelProperty(value = "数据类型", notes = "")
    private String dataType;

}