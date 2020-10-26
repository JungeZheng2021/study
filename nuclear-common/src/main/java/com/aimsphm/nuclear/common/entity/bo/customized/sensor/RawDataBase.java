package com.aimsphm.nuclear.common.entity.bo.customized.sensor;


import lombok.Data;

import java.io.Serializable;

@Data
public class RawDataBase implements Serializable {
    private static final long serialVersionUID = -5658168073551554412L;
    private String tag;
    private Double[] data;
}
