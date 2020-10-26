package com.aimsphm.nuclear.core.vo;

import java.util.List;
import java.util.Map;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.core.vo
 * @Description: <主泵系统总览VO>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:46
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PumpPanoramaVO {
    /**
     * 设备map
     */
    private List<TxPumpsnapshot> devices;
    /**
     * 测点map
     */
    private List<Map<String, MeasurePointVO>> points;
    /**
     * 公共测点
     */
    private Map<String, MeasurePointVO> commPoints;
    /**
     * 设备状态信息 --> 健康,待观察,停机,预警,报警
     */
    private int[] healthInfo = new int[5];
}
