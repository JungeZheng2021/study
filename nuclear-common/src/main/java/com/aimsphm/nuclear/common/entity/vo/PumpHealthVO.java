package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/7 17:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/7 17:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PumpHealthVO {
    /**
     * 健康状态
     */
    private Integer status;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private Long deviceId;
    /**
     * 时间戳
     */
    private Long timestamp;

}
