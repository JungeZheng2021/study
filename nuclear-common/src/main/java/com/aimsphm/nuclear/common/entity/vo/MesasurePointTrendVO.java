package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

@Data
public class MesasurePointTrendVO extends MeasurePointVO {
	private Integer trend;
	private Double changeAmplitude;
}
