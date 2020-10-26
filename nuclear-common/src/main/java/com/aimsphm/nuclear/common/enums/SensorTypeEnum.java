package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

public enum SensorTypeEnum {

	//-1其他相关[业务中使用] 0.流量 1.温度 2.转速和电信号 3.振动 4.振动特征-1X 5.振动特征-2X 6.额外公共量 7.报警测点
	SOMETHING_ELSE((byte) -1, "其他相关"),
	Flow((byte) 0, "流量相关"),
	Temp((byte) 1, "温度相关"),
	SpeedOrElectric((byte) 2, "转速与电信号"),
	Vibration((byte) 3, "振动相关"),
	VibrationF1X((byte) 4, "振动一倍相关"),
	VibrationF2X((byte) 5, "振动二倍相关"),
	OtherPublic((byte) 6, "额外公共量"),
	ALARM((byte) 7, "报警相关");

	SensorTypeEnum(Byte value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	public static String getValue(String desc) {
		if (desc == null) {
			return null;
		}

		SensorTypeEnum[] instances = SensorTypeEnum.values();
		for (SensorTypeEnum i : instances) {
			if (desc != null && desc.equals(i.getDesc())) {
				if(i == SensorTypeEnum.SOMETHING_ELSE)
				{
					return "8";
				}
				return i.getValue().toString();
			}
		}
		return null;
	}
	public static String getDesc(byte value) {
		SensorTypeEnum typeEnum = getByValue(value);
		if (Objects.isNull(typeEnum)) {
			return SensorTypeEnum.SOMETHING_ELSE.getDesc();
		}
		return typeEnum.getDesc();
	}

	public static SensorTypeEnum getByValue(Byte value) {
		if (value == null) {
			return null;
		}

		SensorTypeEnum[] instances = SensorTypeEnum.values();
		for (SensorTypeEnum i : instances) {
			if (value != null && value.equals(i.getValue())) {
				return i;
			}
		}

		return null;
	}

	public Byte getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	private Byte value;

	private String desc;
}
