package com.study.common.entity.bo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.study.common.entity.bo
 * @Description: <查询实体类>
 * @Author: milla
 * @CreateDate: 2020/11/06 14:56
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/06 14:56
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "查询实体类")
public class QueryBO<T> {

    @ApiModelProperty(value = "分页实体", notes = "包含分页参数")
    private Page<T> page;

    @ApiModelProperty(value = "实体参数", notes = "")
    private T entity;

    public QueryWrapper initQueryWrapper() {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.setEntity(entity);
        return wrapper;
    }
}
