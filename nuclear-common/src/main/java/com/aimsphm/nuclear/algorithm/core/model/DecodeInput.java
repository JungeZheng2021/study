package com.aimsphm.nuclear.algorithm.core.model;

import java.util.List;

import lombok.Data;

@Data     
public class DecodeInput {
	private Integer decodeType;
	private List<Integer> alarmCode;
}
