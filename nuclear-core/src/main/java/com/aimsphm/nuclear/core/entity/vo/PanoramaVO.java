package com.aimsphm.nuclear.core.entity.vo;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.vo
 * @Description: <系统总览VO>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:46
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PanoramaVO extends JobDeviceStatusDO {

    @ApiModelProperty(value = "需要显示的条目", notes = "条目是有序的")
    private Map<String, String> items;

}
