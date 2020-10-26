package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-11
 */
@Data
public class MdSensorExtrainfo extends ModelBase {

private Long sensorId;
private String tagId;
private String sensorModel;
private String sensorSerial;
private Long productId;
private Long edgeId;
private Integer firmware;
private Float temp1;
private Float temp2;
private Double voltage;
private Long unitId;
private Integer sleepTime;
private Integer daqFrequency;
private Integer daqTime;
private String apSsid;
private String apKey;
private String clientIp;
private String clientMask;
private String clientGateway;
private String tcpServerIp;
private String tcpServerPort;
private Integer status;
private Double fmin;
private Double fmax;
private Double vtEarlyWarningLo;
private Double vtThrLo;
private Double vtThrLolo;
private Double t1EarlyWarningHi;
private Double t1ThrHi;
private Double t1ThrHihi;
private Double t2EarlyWarningHi;
private Double t2ThrHi;
private Double t2ThrHihi;
private Integer transferType;
private Double sensitivity;
private Integer connectStatus;
//配置状态 1:配置中 2:成功 3:失败 4:超时
private Integer configStatus;
private Integer mode;
@TableField(exist = false)
private Integer iswifi;
@TableField(exist = false)
private String alias;
}