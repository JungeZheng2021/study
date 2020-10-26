package com.aimsphm.nuclear.common.entity;

import java.util.List;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.ModelBase;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Data
public class MdSubSystem extends ModelBase {
    private static final long serialVersionUID = -4459757128224957950L;
    private Long systemId;
    private String subSystemName;
    private String subSystemDesc;
    private Integer subSystemType;//0:主泵 1:气机 2:水泵 3：换热器 4：调节阀 5：风机 6:变压器
    private Integer additionalType;//0 “设备冷却水泵”，1 :“主给水泵”, 2 “循环水泵及油泵” 4"厂用水泵" 5"安全壳再循环风机"
    private Long trendfAlgoId;
    private Long trendrAlgoId;
    private Long forecastAlgoId;
    private Long faultdAlgoId;
    @TableField(exist = false)
    private List<MdDevice> mdDevice;
    private Long vifAlgoId;
}