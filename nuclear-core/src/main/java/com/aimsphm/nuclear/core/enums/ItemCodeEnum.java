package com.aimsphm.nuclear.core.enums;

public enum ItemCodeEnum {
	SurfaceHardness("00001");
	private ItemCodeEnum(String value) {
		this.value = value;
	}
	
	public static ItemCodeEnum getByValue(String value) {
		if (value == null) {
			return null;
		}
		
		ItemCodeEnum[] instances = ItemCodeEnum.values();
		for (ItemCodeEnum i: instances) {
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
