package com.aimsphm.nuclear.common.enums;

public enum DeviceTypeEnum {
	Pump(0),
	Turbine(1),
	Rotating(2);
	private DeviceTypeEnum(Integer value) {
		this.value = value;
	}
	
	public static DeviceTypeEnum getByValue(Integer value) {
		if (value == null) {
			return null;
		}
		
		DeviceTypeEnum[] instances = DeviceTypeEnum.values();
		for (DeviceTypeEnum i: instances) {
			if (value != null && value.equals(i.getValue())) {
				return i;
			}
		}
		
		return null;
	}
	
	public Integer getValue() {
		return value;
	}

	private Integer value;
}
