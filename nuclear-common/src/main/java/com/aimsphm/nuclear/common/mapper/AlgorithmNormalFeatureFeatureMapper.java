package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFeatureFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.mapper
 * @Description: <Mapper接口>
 * @Author: MILLA
 * @CreateDate: 2021-06-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmNormalFeatureFeatureMapper extends BaseMapper<AlgorithmNormalFeatureFeatureDO> {

    /**
     * 查询征兆之间的关联度信息集合
     *
     * @param device
     * @return
     */
    List<SymptomCorrelationVO> listSymptomCorrelationVO(@Param("device") CommonDeviceDO device);
}
