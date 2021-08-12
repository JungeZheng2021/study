package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <多个测点>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:33
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "历史查询多个测点")
public class HistoryQueryMultiBO {
    @ApiModelProperty(value = "测点列表", notes = "")
    private List<String> pointIds;
    @ApiModelProperty(value = "模型id", notes = "查询实测值、估计值、残差时会用到")
    private Long modelId;
    @ApiModelProperty(value = "设备id", notes = "查询实测值、估计值、残差时会用到")
    private Long deviceId;
    @ApiModelProperty(value = "开始时间")
    private Long start;
    @ApiModelProperty(value = "结束时间")
    private Long end;

    public String key() {
        return this.getPointIds().stream().sorted().collect(Collectors.joining()).concat(String.valueOf(this.getStart())).concat(String.valueOf(this.getEnd()));
    }

}
