package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


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
    /**
     * 测点值
     */
    private Double value;

    /**
     * 测点状态
     */
    private Byte status;

    /**
     * 状态原因
     */
    @TableField(exist = false)
    private String statusCause;


    public String getDesc() {
        return PointCategoryEnum.getDesc(this.getCategory().byteValue());
    }

}
