package com.aimsphm.nuclear.core.vo;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.vo
 * @Description: <旋机检测显示类>
 * @Author: milla
 * @CreateDate: 2020/08/13 18:22
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/13 18:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class RotaryMonitorVO {
    /**
     * 需要展示的测点信息
     */
    private Map<String, MeasurePointVO> points;

    /**
     * 其中的最值信息 -第一个是所有的最大环境温度 其余不计顺序
     */
    private List<MeasurePointVO> maxPoints;
}
