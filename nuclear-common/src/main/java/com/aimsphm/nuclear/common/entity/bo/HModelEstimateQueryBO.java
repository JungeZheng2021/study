package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity.bo
 * @Description: <查询参数>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 15:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HModelEstimateQueryBO {

	@ApiModelProperty(value = "模型id")
	private String modelId;
	@ApiModelProperty(value = "结束行")
	private Long startTime;
	@ApiModelProperty(value = "开始行")
	private Long endTime;
	@ApiModelProperty(value = "指定值")
	private String tagId;
}
