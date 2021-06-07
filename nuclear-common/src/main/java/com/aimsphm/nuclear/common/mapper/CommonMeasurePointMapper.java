package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionParamDTO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.mapper
 * @Description: <测点信息Mapper接口>
 * @Author: MILLA
 * @CreateDate: 2020-12-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonMeasurePointMapper extends BaseMapper<CommonMeasurePointDO> {

    /**
     * 查询需要进行特征计算的点
     *
     * @param pointType
     * @return
     */
    List<FeatureExtractionParamDTO> listFeatureExtraction(@Param("pointType") Integer pointType);
}
