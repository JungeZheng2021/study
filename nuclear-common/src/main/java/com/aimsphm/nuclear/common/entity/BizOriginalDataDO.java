package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <波形数据信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-02-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_original_data")
@ApiModel(value = "波形数据信息实体")
public class BizOriginalDataDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5920745986835987713L;

    @ApiModelProperty(value = "数据采集时间", notes = "")
    private Long timestamp;

    @ApiModelProperty(value = "数据类型", notes = "")
    private Integer dataType;

    @ApiModelProperty(value = "传感器编号", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}