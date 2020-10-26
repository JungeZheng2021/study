package com.aimsphm.nuclear.algorithm.core.model;

import com.aimsphm.nuclear.common.entity.dto.Cell;
import lombok.Data;

import java.util.List;

@Data     
public class SensorCheckInput {
	private SensorOriginData sensorOriginData;
	private Double curFeature;
	private List<Cell> histFeatures;
	private Integer workCond;
	private List<Cell> histWorkCond;
}
