package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 10:04
 */
@Data
public class DeviceDetailMetaDTO {
    private String filedNameEn;
    private String filedNameZh;
    private String fieldType;
    private String fieldValue;

    public DeviceDetailMetaDTO() {
        super();
    }

    public DeviceDetailMetaDTO(String fne, String fnz, String ft, String fv) {
        this.filedNameEn = fne;
        this.filedNameZh = fnz;
        this.fieldType = ft;
        this.fieldValue = fv;
    }
}
