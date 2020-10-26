package com.aimsphm.nuclear.core.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Data
public class MdPumpinfo extends ModelBase {
    private static final long serialVersionUID = 3394530923502950486L;
private Long deviceId;
private String pumpType;
private String lastRepairTime;
private String pumpMaterial;
private String rateFlow;
private String rateHead;
private String designPressure;
private String designTemperature;
private String coolingWaterInletTemperature;
private String outletNozzleInnerDiameter;
private String inletNozzleInnerDiameter;
private String designLife;
private String systemPower;
private String operationPressure;
private String primaryWaterCapacity;
}