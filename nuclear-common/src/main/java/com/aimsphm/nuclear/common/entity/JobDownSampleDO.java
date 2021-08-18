package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <等间隔降采样数据实体>
 * @Author: MILLA
 * @CreateDate: 2021-07-28
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_down_sample")
@ApiModel(value = "等间隔降采样数据实体")
public class JobDownSampleDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6489662348408452665L;

    @ApiModelProperty(value = "部件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "测点id", notes = "")
    private String pointId;

    @ApiModelProperty(value = "数据", notes = "")
    private String data;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}