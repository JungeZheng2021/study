package com.aimsphm.nuclear.algorithm.core.model;

import com.aimsphm.nuclear.common.entity.dto.Cell;
import lombok.Data;

import java.util.List;

@Data     
public class SensorCheckOutput {
	private String deviceId;
	private String sensorId;
	private Integer sensorCond;
}
