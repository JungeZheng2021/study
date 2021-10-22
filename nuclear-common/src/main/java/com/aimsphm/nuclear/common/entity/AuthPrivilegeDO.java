package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <权限资源信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-05-06
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-05-06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("auth_privilege")
@ApiModel(value = "权限资源信息实体")
public class AuthPrivilegeDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6151788532592707358L;

    @ApiModelProperty(value = "权限代码-对接田湾权限code", notes = "")
    private String code;

    @ApiModelProperty(value = "名称", notes = "")
    private String name;

    @ApiModelProperty(value = "资源url", notes = "")
    private String url;

    @ApiModelProperty(value = "定义", notes = "")
    private String definition;

    @ApiModelProperty(value = "描述", notes = "")
    private String description;

    @ApiModelProperty(value = "父节点id", notes = "")
    private Long parentId;

    @ApiModelProperty(value = "数据类型", notes = "数据权限：0 资源权限：1")
    private Integer category;

    @ApiModelProperty(value = "类型", notes = "功能:0  Tab:1  Button:2")
    private Integer type;

    @TableField(exist = false)
    /**
     *子权限
     */
    private List<AuthPrivilegeDO> children;


}