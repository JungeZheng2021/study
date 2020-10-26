package com.aimsphm.nuclear.data.enums;

/**
 * @Package: com.aimsphm.nuclear.data.enums
 * @Description: <传感器类型枚举>
 * @Author: milla
 * @CreateDate: 2020/10/22 17:51
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/22 17:51
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum SensorDataCategoryEnum {
    /**
     * 传感器状态
     */
    CONNECT_STATUS(11),
    /**
     * 波形数据
     */
    WAVEFORM_DATA(12),
    /**
     * 油质常规数据
     */
    OIL_NORMAL_DATA(13),
    /**
     * 振动派生数据
     */
    VIBRATION_DERIVE_DATA(14),
    /**
     * 声学派生数据
     */
    ACOUSTICS_DERIVE_DATA(15),
    /**
     * 油质传感器设置
     */
    OIL_SETTINGS(21),
    /**
     * 油质传感器设置结果
     */
    OIL_SETTING_STATUS(16),
    /**
     * 波形采集设置
     */
    WAVEFORM_SETTINGS(22),
    /**
     * 波形采集设置结果
     */
    WAVEFORM_SETTINGS_STATUS(17);


    private Integer type;

    SensorDataCategoryEnum(int type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
