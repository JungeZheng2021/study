package com.aimsphm.nuclear.common.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class EstimateTotal {
	private List<EstimateResult> estimateResults = new ArrayList<>();
}
