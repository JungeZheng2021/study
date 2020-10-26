package com.aimsphm.nuclear.algorithm.core.model;

import lombok.Data;

import java.util.List;

@Data     
public class SensorOriginData {

	private String deviceId;
	private String sensorId;
	private Long timestamp;
	private Integer daqTime;
	private Integer fs;
	private Double[] cells;
}
