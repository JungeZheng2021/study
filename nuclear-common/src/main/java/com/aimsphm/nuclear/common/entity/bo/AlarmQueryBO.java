package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.entity.bo
 * @Description: <报警事件查询参数类>
 * @Author: MILLA
 * @CreateDate: 2020/12/04 17:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/04 17:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlarmQueryBO extends ConditionsQueryBO {

    @ApiModelProperty(value = "事件分类", notes = "可多选")
    private List<Integer> alarmStatusList;

    @ApiModelProperty(value = "报警界别", notes = "可多选")
    private List<Integer> alarmLevelList;

    @ApiModelProperty(value = "处理状态", notes = "可多选")
    private List<Integer> operateStatusList;

    @ApiModelProperty(value = "持续时间", notes = "")
    private Integer duration;

    public AlarmQueryBO() {
        init();
    }

    private void init() {
        alarmStatusList = new ArrayList<>();
        alarmLevelList = new ArrayList<>();
        operateStatusList = new ArrayList<>();
    }
}
