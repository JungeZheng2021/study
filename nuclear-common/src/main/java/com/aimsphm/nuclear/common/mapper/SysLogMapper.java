package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.SysLog;
import com.aimsphm.nuclear.common.entity.bo.SysLogQueryBo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysLogMapper extends BaseMapper<SysLog> {
    public List<SysLog> getSysLogListByInfo(@Param("sysLogQueryBo") SysLogQueryBo sysLogQueryBo, Page<SysLog> page);
}
