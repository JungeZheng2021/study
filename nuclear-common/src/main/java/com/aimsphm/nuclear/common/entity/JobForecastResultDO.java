package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <预测结果信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-07-16
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_forecast_result")
@ApiModel(value = "预测结果信息实体")
public class JobForecastResultDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6281211648209133890L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "部件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "推理结果", notes = "")
    private String symptomIds;

    @ApiModelProperty(value = "预测时间", notes = "")
    private Date gmtForecast;

    @ApiModelProperty(value = "预测周期", notes = "")
    private String forecastRange;

    @ApiModelProperty(value = "测点id", notes = "")
    private String pointId;

    @ApiModelProperty(value = "历史实测值", notes = "")
    private String historyData;

    @ApiModelProperty(value = "滑动平均数据", notes = "")
    private String trendData;

    @ApiModelProperty(value = "预测数据", notes = "")
    private String forecastData;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}