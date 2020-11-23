package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-11
 */
@Data
public class WiFiSensorExtrainfoVO  {
    private String sensorType = "单轴无线振动温度传感器";
    private String sensorModel;
    private String sensorSerial;
    private String tagId;
    private Integer status;
    private Integer connectStatus;
    private Double voltage;
    private Integer daqFrequency;
    private Integer sleepTime;
    private Integer daqTime;
    private String transmissionType = "Wi-Fi";
    private String clientIp;
    private String apSsid;
    private String apKey;
    private String tcpServerIp;
    private String tcpServerPort;
    private String clientGateway;
    private String clientMask;
    private String measuringDistance="+-50g";
    private String aTemperatureMeasurement = "-40℃~85℃";
    private String eTemperatureMeasurement = "-40℃~115℃";
    private String installationMethod = "将磁座通过胶粘方式与设备固定,传感器与磁座螺栓固定";
    private Double vtThrLo;
    private Integer iswifi;
    private String alias;
    private Double remainingPower;
//private String sensorSerial;
//private Long productId;
//private Long edgeId;
//private Integer firmware;
//private Float temp1;
//private Float temp2;
//
//private Long unitId;
//
//
//
//
//
//
//
//
//
//
//
//private Double fmin;
//private Double fmax;
//private Double vtEarlyWarningLo;
//private Double vtThrLo;
//private Double vtThrLolo;
//private Double t1EarlyWarningHi;
//private Double t1ThrHi;
//private Double t1ThrHihi;
//private Double t2EarlyWarningHi;
//private Double t2ThrHi;
//private Double t2ThrHihi;
//private Integer transferType;
//private Double sensitivity;

}