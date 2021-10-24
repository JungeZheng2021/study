package com.aimsphm.nuclear.common.entity.dto.vibanalysisoutput;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 10:04
 */
@Data
public class AnalysisOutputDTO implements Serializable {

    private static final long serialVersionUID = 5765531784513971378L;
    private String deviceId;
    private String sensorId;
    private List<List<Object>> domainData;

    private List<List<Object>> fftData;

    private List<List<Object>> hilData;
    private List<List<List<Object>>> dtWpData;
    private List<List<List<Object>>> dtWpFFTData;
    private List<List<List<Object>>> dtWpHILData;
    private String warning;


}
