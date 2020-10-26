package com.aimsphm.nuclear.common.autoIncrKey;


public enum BizAutoIncrKey {
	BATCH_UPDATE_ID("batch_update_id"),
	TASK_ID("task_id"),
	TEST("test_id");
	
	private BizAutoIncrKey(String value) {
		this.value = value;
	}
	
	public static BizAutoIncrKey getByValue(Integer value) {
		if (value == null) {
			return null;
		}
		
		BizAutoIncrKey[] instances = BizAutoIncrKey.values();
		for (BizAutoIncrKey i: instances) {
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
