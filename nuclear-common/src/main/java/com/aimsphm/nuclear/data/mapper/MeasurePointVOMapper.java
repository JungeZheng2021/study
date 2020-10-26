package com.aimsphm.nuclear.data.mapper;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurePointVOMapper {

    List<MeasurePointVO> selectMeasurePoints(@Param("category") int category);

    List<MeasurePointVO> selectMeasurePointsByTagId(@Param("category") int category, @Param("tagId") String tagId);
}
