package com.aimsphm.nuclear.data.feign.entity.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.data.entity.bo
 * @Description: <数据上传到MQ的结构>
 * @Author: MILLA
 * @CreateDate: 2020/10/22 18:46
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/10/22 18:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PacketDTO {
    /**
     * 传感器编号
     */
    private String sensorCode;
    /**
     * 采样频率（Hz）
     */
    private String acqFrequency;
    /**
     * 采样时长（秒）
     */
    private String acqTime;
    /**
     * 采样间隔时间（秒）
     */
    private Long sleepTime;
    /**
     * 数据时间戳
     */
    private Long timestamp;
    /**
     * 通道的状态信息
     * 0：该测点数据派生正常（算法调用成功），
     * 1：该测点数据派生异常（算法调用失败）
     */
    private String tagStatus;
    /**
     * 振动原始数据
     */
    private Double[] data;
    /**
     * 振动计算得到数据
     */
    private Double[] vecData;
    /**
     * 派生测点数值
     */
    private Map<String, Double> featuresResult;
    /**
     * 秒级别rms值
     */
    private Double vecRms;


    /**
     * -----配置信息参数-----------
     * 边缘端编号
     */
    private String edgeCode;
    /**
     * 配置信息结果
     */
    private ConfigSettingsDTO configResult;
}
