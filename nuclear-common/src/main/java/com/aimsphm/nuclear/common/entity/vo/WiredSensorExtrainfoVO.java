package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-08-11
 */
@Data
public class WiredSensorExtrainfoVO {
    private String sensorType = "有线传感器";
    private String sensorModel;
    private String sensorSerial;
    private String tagId;
    private Integer connectStatus;
    private Integer status;
    private Integer daqFrequency;
    private Integer sleepTime;
    private Integer daqTime;
    private String transmissionType = "有线";
    private String tcpServerIp;
    private String tcpServerPort;
    private String apSsid;
    private String apKey;
    private String installationMethod = "将磁座通过胶粘方式与设备固定,传感器与磁座螺栓固定";
    private Double vtThrLo;
    private Integer iswifi;
    private String alias;
}