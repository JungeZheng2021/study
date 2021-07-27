package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.mapper
 * @Description: <预测结果信息Mapper接口>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobForecastResultMapper extends BaseMapper<JobForecastResultDO> {

    /**
     * 根据设备id查询可以进行性能预测的部件信息
     *
     * @param deviceId 设备id
     * @return
     */
    List<CommonComponentDO> listCommonComponentByDeviceId(@Param("deviceId") Long deviceId);
}
