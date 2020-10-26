package com.aimsphm.nuclear.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class SysLogResult {
    private Long id;
    private String userName;
    private String operation;
    private String method;
    private String methodType;
    private String params;
    private String ip;
    private String serverIp;
    private String methodCnType;
    private String url;
//    @JsonSerialize(using = LocalDateTimeConverter.class)
//    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    public Long getTime() {
        return time.getTime();
    }
}
