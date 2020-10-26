package com.aimsphm.nuclear.common.entity.bo.customized.sensor;

import lombok.Data;

import java.io.Serializable;

@Data
public class FwsRawData extends RawDataBase implements Serializable {
    private static final long serialVersionUID = 8409151610343171826L;
    private Integer channelStatus;
}
