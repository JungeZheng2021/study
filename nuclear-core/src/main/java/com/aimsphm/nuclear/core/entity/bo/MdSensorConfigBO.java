package com.aimsphm.nuclear.core.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MdSensorConfigBO {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "传感器id")
    private Long sensorId;
    @ApiModelProperty(value = "传感器编号(暂弃)")
    private String tagId;
    @ApiModelProperty(value = "传感器编号")
    private String alias;
    @ApiModelProperty(value = "状态(配置状态 1:配置中 2:成功 3:失败 4:超时)")
    private Integer configStatus;
    @ApiModelProperty(value = "传感器型号")
    private String sensorModel;
    @ApiModelProperty(value = "序列号")
    private String sensorSerial;
    @ApiModelProperty(value = "灵敏度")
    private Double sensitivity;
    @ApiModelProperty(value = "配置方式(0：自动；1：手动)")
    private Integer mode;
    @ApiModelProperty(value = "所属部件")
    private String sensorDes;
    @ApiModelProperty(value = "isWifi(0：有线；1：wifi)")
    private Boolean isWifi;

    @ApiModelProperty(value = "分组类：电机/泵")
    public String getGroupName() {
        if (sensorDes.startsWith("M")) {
            return "电机";
        } else if (sensorDes.startsWith("P")) {
            return "泵";
        }else if(sensorDes.startsWith("G"))
        {
            return "增速箱";

        }
        return "其它";
    }
}
