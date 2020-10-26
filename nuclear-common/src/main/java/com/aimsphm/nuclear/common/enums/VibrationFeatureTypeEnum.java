package com.aimsphm.nuclear.common.enums;

public enum VibrationFeatureTypeEnum {
	CWS_O("cws_o"),
	CWS_W("cws_w"),
	CCS("ccs"),
	SWS("sws"),
	FWS("fws");
	private VibrationFeatureTypeEnum(String value) {
		this.value = value;
	}
	
	public static VibrationFeatureTypeEnum getByValue(Integer value) {
		if (value == null) {
			return null;
		}
		
		VibrationFeatureTypeEnum[] instances = VibrationFeatureTypeEnum.values();
		for (VibrationFeatureTypeEnum i: instances) {
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
