package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/8 13:12
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/8 13:12
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class WarmingPointsVO {
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 统计类型
     */

    private String type;
    /**
     * 出现次数
     */
    private Integer time;

}
