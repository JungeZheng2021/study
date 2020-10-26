package com.aimsphm.nuclear.algorithm.core.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DecodeOutput {
	private Integer decodeType;
	private List<Integer> featureType;
	private Object featureDesc;
}
