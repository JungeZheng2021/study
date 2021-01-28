package com.aimsphm.nuclear.data.feign.entity.dto;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.data.feign.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 11:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 11:10
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PublishParamDTO {

    /**
     * 需要下发的设置
     */
    private ConfigSettingsPacketDTO settings;
    /**
     * 队列配置
     */
    private MqConfigDTO config;

    public PublishParamDTO() {
        initConfig();
    }

    private void initConfig() {
        this.config = new MqConfigDTO();
        this.config.setExchange("amq.topic");
        this.config.setRoutingKey("JSNPC.Download");

    }
}
