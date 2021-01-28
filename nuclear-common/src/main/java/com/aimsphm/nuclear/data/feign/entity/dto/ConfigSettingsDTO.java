package com.aimsphm.nuclear.data.feign.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.data.feign.entity.dto
 * @Description: <油液配置>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 11:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 11:10
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class ConfigSettingsDTO {

    @ApiModelProperty(value = "磨损传感器清零设置", notes = "0：不进行清零 1：数据清零")
    private Integer oilResetAbrasion;

    @ApiModelProperty(value = "油液粘度计算方法", notes = "0: 原始数据（不标定） 1: 320中自庆安版 2: 320标准版")
    private Integer oilViscosityCalculMethod;

    @ApiModelProperty(value = "油液粘度计算方法", notes = "0: 原始数据（不标定） 1: 320中自庆安版 2: 320标准版")
    private Long oilSleepTime;

    @ApiModelProperty(value = "全部油质数据上传请求", notes = "0: 上传 1: 不上传")
    private Long oilUploadRequest;

    private Long soundValueSleepTime;

    private Long soundValueUploadRequest;

    private Long soundWaveSleepTime;

    private Long soundWaveUploadRequest;

    @ApiModelProperty(value = "全部振动特征值上传周期", notes = "单位秒")
    private Long vibrationValueSleepTime;

    @ApiModelProperty(value = "全部振动特征值上传请求", notes = "0: 上传 1: 不上传")
    private Long vibrationValueUploadRequest;

    @ApiModelProperty(value = "全部振动波形上传周期", notes = "单位秒")
    private Long vibrationWaveSleepTime;

    @ApiModelProperty(value = "振动波形采集时长", notes = "单位秒")
    private Long vibrationWaveAcquisitionTime;

    private Map<String, Double> vibrationSensitivity;

    private List<String> vibrationWaveUploadRequest;

}
