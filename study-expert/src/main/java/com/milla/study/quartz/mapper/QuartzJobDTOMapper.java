package com.milla.study.quartz.mapper;


import com.milla.study.quartz.dto.QuartzJobDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuartzJobDTOMapper {
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