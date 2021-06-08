package com.aimsphm.nuclear.common.entity.dto.vibanalysisoutput;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnalysisOutputDTO implements Serializable {

    private static final long serialVersionUID = 5765531784513971378L;
    private String deviceId;
    private String sensorId;
    private List<List<Object>> domainData;

    private List<List<Object>> fftData;

    private List<List<Object>> hilData;
    private List<List<List<Object>>> dtwpData;
    private List<List<List<Object>>> dtwpFFTData;
    private List<List<List<Object>>> dtwpHILData;
    private String warning;


}
