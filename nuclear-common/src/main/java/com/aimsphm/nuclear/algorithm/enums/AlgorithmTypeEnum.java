package com.aimsphm.nuclear.algorithm.enums;


public enum AlgorithmTypeEnum {
	AlarmAlgorithm("00001"),
	TrendForeCast("00002"),
	TrendRecognition("00003"),
	TrendFeature("00004"),
	FaultDiagnosis("00005"),
	Decode("00006"),


	VibrationAnalysis("00009"),


	VibrationFeature("00010"),
	SensorStatusCheck("00011");
	private AlgorithmTypeEnum(String value) {
		this.value = value;
	}
	
	public static AlgorithmTypeEnum getByValue(String value) {
		if (value == null) {
			return null;
		}
		
		AlgorithmTypeEnum[] instances = AlgorithmTypeEnum.values();
		for (AlgorithmTypeEnum i: instances) {
			if (value != null && value.equals(i.getValue())) {
				return i;
			}
		}
		
		return null;
	}
	
	public String getValue() {
		return value;
	}

	private String value;
}
