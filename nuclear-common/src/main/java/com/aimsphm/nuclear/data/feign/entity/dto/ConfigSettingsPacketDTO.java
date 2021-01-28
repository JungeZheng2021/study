package com.aimsphm.nuclear.data.feign.entity.dto;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.data.feign.entity.dto
 * @Description: <油液配置>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 11:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 11:10
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class ConfigSettingsPacketDTO<T> {

    /**
     * 类型
     */
    private Integer type;
    /**
     * 数据包
     */
    private T packet;

}
