package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum VibrationFeatureTypeEnum {
    CWS_O("cws_o"),
    CWS_W("cws_w"),
    CCS("ccs"),
    SWS("sws"),
    FWS("fws");

    VibrationFeatureTypeEnum(String value) {
        this.value = value;
    }

    public static VibrationFeatureTypeEnum getByValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        VibrationFeatureTypeEnum[] instances = VibrationFeatureTypeEnum.values();
        for (VibrationFeatureTypeEnum i : instances) {
            if (i.getValue().equals(value)) {
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
