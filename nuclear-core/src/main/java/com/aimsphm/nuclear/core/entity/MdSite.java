package com.aimsphm.nuclear.core.entity;

import java.util.List;

import com.aimsphm.nuclear.common.entity.ModelBase;
import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Data
public class MdSite extends ModelBase {
    private static final long serialVersionUID = 7156227670078358802L;
private String siteName;
private String siteDesc;
private Integer parentDistrict;
@TableField(exist = false)
private List<MdSet> sets;
}