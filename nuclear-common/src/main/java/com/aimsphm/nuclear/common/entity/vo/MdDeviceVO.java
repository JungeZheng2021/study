package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/13 17:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/13 17:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class MdDeviceVO {
    /**
     * 主键
     */
    protected Long id;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 设备的附加类型
     */
    private Integer additionalType;

}
