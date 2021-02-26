package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;


/**
 * @Package: com.aimsphm.nuclear.data.entity.dto
 * @Description: <测点信息>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 13:43
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 13:43
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class MeasurePointVO extends CommonMeasurePointDO {

    private static final long serialVersionUID = -2071565876962058344L;

    @ApiModelProperty(value = "测点值", notes = "")
    private Double value;

    @ApiModelProperty(value = "测点状态", notes = "")
    private Byte status;

    @ApiModelProperty(value = "报警级别", notes = "报警事件表需要字段")
    private Integer alarmLevel;

    @ApiModelProperty(value = "状态原因", notes = "")
    private String statusCause;


    public String getDesc() {
        return PointCategoryEnum.getDesc(this.getCategory());
    }

    public String getStatusDesc() {
        return Objects.isNull(status) ? "正常" : "异常";
    }

}
