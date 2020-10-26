package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;

@Data
public class DeviceDetailMetaDTO {
    private String filedNameEn;
    private String filedNameZh;
    private String fieldType;
    private String fieldValue;

    public DeviceDetailMetaDTO(){
        super();
    }

    public DeviceDetailMetaDTO(String fne,String fnz,String ft,String fv){
        this.filedNameEn=fne;
        this.filedNameZh=fnz;
        this.fieldType=ft;
        this.fieldValue=fv;
    }
}
