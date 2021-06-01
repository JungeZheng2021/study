package com.aimsphm.nuclear.algorithm.entity.bo;

import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 15:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 15:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PointDataBO {
    @ApiModelProperty(value = "测点编号", notes = "")
    private String pointId;
    @ApiModelProperty(value = "原始数据", notes = "")
    private List<HBaseTimeSeriesDataDTO> cells;

}
