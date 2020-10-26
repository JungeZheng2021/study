package com.aimsphm.nuclear.data.entity.dto;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.data.entity.bo
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/22 18:46
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/22 18:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class SensorDataDTO {
    /**
     * 类型
     */
    private Integer type;
    /**
     * 数据包
     */
    private PacketDTO packet;

}