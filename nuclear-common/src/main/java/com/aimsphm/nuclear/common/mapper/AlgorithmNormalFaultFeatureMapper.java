package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.vo.AlgorithmNormalFaultFeatureVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Package: com.aimsphm.nuclear.common.mapper
 * @Description: <Mapper接口>
 * @Author: MILLA
 * @CreateDate: 2021-06-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmNormalFaultFeatureMapper extends BaseMapper<AlgorithmNormalFaultFeatureDO> {

    AlgorithmNormalFaultFeatureVO getAlgorithmNormalFaultFeatureByComponentId(@Param("compmontId") Long compmontId);
}
