package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <等间隔降采样数据实体>
 * @Author: MILLA
 * @CreateDate: 2021-07-27
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_down_sample")
@ApiModel(value = "等间隔降采样数据实体")
public class BizDownSampleDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6489662348408452665L;

    @ApiModelProperty(value = "数据采集时间", notes = "")
    private Long timestamp;

    @ApiModelProperty(value = "传感器编号", notes = "")
    private String pointId;

    @ApiModelProperty(value = "数据", notes = "")
    private String data;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}