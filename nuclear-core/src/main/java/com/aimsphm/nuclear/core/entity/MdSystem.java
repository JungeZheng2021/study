package com.aimsphm.nuclear.core.entity;

import java.util.List;

import com.aimsphm.nuclear.common.entity.MdSubSystem;
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
public class MdSystem extends ModelBase {
    private static final long serialVersionUID = 1544907916763847411L;
private Long setId;
private String systemName;
private String systemDesc;
@TableField(exist = false)
private List<MdSubSystem> subSystem;
}