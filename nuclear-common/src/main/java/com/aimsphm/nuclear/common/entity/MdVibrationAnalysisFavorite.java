package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.google.common.base.CaseFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 振动分析收藏夹
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Data
public class MdVibrationAnalysisFavorite extends ModelBase {

    /**
     * 测点编号
     */
    @ApiModelProperty(value = "测点编号")
    private String tagId;
    /**
     * 测点位置
     */
    @ApiModelProperty(value = "测点位置")
    private String location;
    /**
     * 所属设备名称
     */
    @ApiModelProperty(value = "所属设备名称")
    private String deviceName;
    /**
     * 所属设备id
     */
    @ApiModelProperty(value = "所属设备id")
    private Long deviceId;

    @ApiModelProperty(value = "所属子系统id")
    private Long subSystemId;
    /**
     * 数据类型
     */
    @ApiModelProperty(value = "数据类型")
    private String dataType;
    /**
     * 数据采集时间
     */
    @ApiModelProperty(value = "数据采集时间")
    private Long acquisitionTime;
    /**
     * 是否有备注
     */
    @ApiModelProperty(value = "是否有备注")
    private Boolean hasRemark;
    /**
     * 排序字段
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "测点别名")
    private String alias;

    @TableField(exist = false)
    @ApiModelProperty(value = "是否是wifi测点")
    private Boolean wifiFlag;
    ;
    /**
     * 排序类型
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "排序类型")
    private String sortOrder;

    /**
     * ---------------------------- 业务字段[原则上不该写在该位置]----------------------------------------
     */
    public String getSortField() {
        if (sortField == null || sortField.length() == 0) {
            return null;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortField);
    }

    public String getSortOrder() {
        if (this.sortOrder == null || this.sortOrder.length() == 0) {
            return "asc";
        }
        return sortOrder.startsWith("asc") ? "asc" : "desc";
    }
}