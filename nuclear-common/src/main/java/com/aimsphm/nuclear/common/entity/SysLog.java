package com.aimsphm.nuclear.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class SysLog extends ModelBase{
    private static final long serialVersionUID = -1557325180736285433L;
    private String userName;
    private String operation;
    private String method;
    private String methodCnType;
    private String methodType;
    private String params;
    private String ip;
    private String serverIp;
    private String url;
    private Date time;
}
