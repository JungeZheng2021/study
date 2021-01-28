package com.aimsphm.nuclear.data.feign.entity.dto;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.data.feign.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 17:00
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 17:00
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class MqConfigDTO {

    private String exchange;

    private String routingKey;
}
