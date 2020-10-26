package com.aimsphm.nuclear.common.mapper;


import com.aimsphm.nuclear.common.quartz.dto.QuartzJobDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartzJobDTOMapper extends BaseMapper<QuartzJobDTO> {
    int deleteByPrimaryKey(Integer id);

    int insert(QuartzJobDTO record);

    int insertSelective(QuartzJobDTO record);

    QuartzJobDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuartzJobDTO record);

    int updateByPrimaryKey(QuartzJobDTO record);

    List<QuartzJobDTO> selectJob();

    List<QuartzJobDTO> selectJobByGroup(@Param("report") String report);

    QuartzJobDTO selectByJobName(QuartzJobDTO job);
}