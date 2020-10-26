package com.aimsphm.nuclear.common.entity.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SysLogQueryBo {
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value = "用户")
    private String userName;
    @ApiModelProperty(value = "请求类型")
    private String methodType;
    @ApiModelProperty(value = "请求类型中文")
    private String methodCnType;
    @ApiModelProperty(value = "关键词")
    private String keyword;
    @ApiModelProperty(value = "请求类型码")
    private Integer methodCnTypeCode;
    @ApiModelProperty(value = "当前页")
    private Integer current;
    @ApiModelProperty(value = "每页条目数")
    private Integer size;
}
