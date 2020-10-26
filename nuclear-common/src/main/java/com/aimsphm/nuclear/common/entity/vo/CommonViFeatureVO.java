package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.enums.AlarmTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <报警事件记录vo>
 * @Author: MILLA
 * @CreateDate: 2020/5/9 9:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/9 9:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class CommonViFeatureVO {

    private String timestamp;
    private Double voltage;
    private Double temp_tem1;
    private Double temp_tem2;
    private Double acc_Rms;
    private Double acc_ZeroPeak;
    private Double acc_PeakPeak;
    private Double acc_Kurtosis;
    private Double acc_05Xrf;
    private Double acc_1Xrf;
    private Double acc_2Xrf;
    private Double acc_3Xrf;
    private Double acc_1Xbf;
    private Double acc_2Xbf;
    private Double acc_P2Inner;
    private Double acc_P2Outer;
    private Double acc_P2Cage;
    private Double acc_P2Roll;
    private Double acc_P1Inner;
    private Double acc_P1Outer;
    private Double acc_P1Cage;
    private Double acc_P1Roll;
    private Double acc_M2Inner;
    private Double acc_M2Outer;
    private Double acc_M2Cage;
    private Double acc_M2Roll;
    private Double acc_M1Inner;
    private Double acc_M1Outer;
    private Double acc_M1Cage;
    private Double acc_M1Roll;
    private Double acc_6Xrf;
    private Double acc_12Xrf;
    private Double acc_1Gmf;
    private Double acc_2Gmf;
    private Double acc_3Gmf;
    private Double acc_1GmfLsf;
    private Double acc_2GmfLsf;
    private Double acc_1GmfRsf;
    private Double acc_2GmfRsf;
    private Double acc_InputDsInner;
    private Double acc_InputDsOuter;
    private Double vec_InputDsCage;
    private Double vec_InputDsRoll;
    private Double accInput_NdsInner;
    private Double accInput_NdsOuter;
    private Double acc_InputNdsCage;
    private Double acc_InputNdsRoll;
    private Double acc_OutputDsInner;
    private Double acc_OutputDsOuter;
    private Double acc_OutputDsCage;
    private Double acc_OutputDsRoll;
    private Double accOutput_NdsInner;
    private Double accOutput_NdsOuter;
    private Double acc_OutputNdsCage;
    private Double acc_OutputNdsRoll;
    private Double vec_Rms;
    private Double vec_Rms10;
    private Double vec_ZeroPeak;
    private Double vec_PeakPeak;
    private Double vec_Kurtosis;
    private Double vec_05Xrf;
    private Double vec_1Xrf;
    private Double vec_2Xrf;
    private Double vec_3Xrf;
    private Double vec_M2Inner;
    private Double vec_M2Outer;
    private Double vec_M2Cage;
    private Double vec_M2Roll;
    private Double vec_M1Inner;
    private Double vec_M1Outer;
    private Double vec_M1Cage;
    private Double vec_M1Roll;
    private Double vec_1Xbf;
    private Double vec_2Xbf;
    private Double vec_P2Inner;
    private Double vec_P2Outer;
    private Double vec_P2Cage;
    private Double vec_P2Roll;
    private Double vec_P1Inner;
    private Double vec_P1Outer;
    private Double vec_P1Cage;
    private Double vec_P1Roll;
    private Double vec_6Xrf;
    private Double vec_12Xrf;
    private Double vec_1Gmf;
    private Double vec_2Gmf;
    private Double vec_3Gmf;
    private Double vec_1GmfLsf;
    private Double vec_2GmfLsf;
    private Double vec_1GmfRsf;
    private Double vec_2GmfRsf;
    private Double vec_InputDsInner;
    private Double vec_InputDsOuter;
    private Double vec_InputNdsInner;
    private Double vec_InputNdsOuter;
    private Double vec_InputNdsCage;
    private Double vec_InputNdsRoll;
    private Double vec_OutputDsInner;
    private Double vec_OutputDsOuter;
    private Double vec_OutputDsCage;
    private Double vec_OutputDsRoll;
    private Double vec_OutputNdsInner;
    private Double vec_OutputNdsOuter;
    private Double vec_OutputNdsCage;
    private Double vec_OutputNdsRoll;
}
