package com.aimsphm.nuclear.common.excelutil;

public enum MasterDataType {
	SYSTEM("0");
	private MasterDataType(String value) {
		this.value = value;
	}
	
	public static MasterDataType getByValue(String value) {
		if (value == null) {
			return null;
		}
		
		MasterDataType[] instances = MasterDataType.values();
		for (MasterDataType i: instances) {
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
