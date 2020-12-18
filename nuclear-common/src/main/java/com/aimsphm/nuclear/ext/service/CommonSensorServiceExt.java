package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <传感器信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonSensorServiceExt extends CommonSensorService {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSensorDO> listCommonSensorByPageWithParams(QueryBO<CommonSensorDO> queryBO);
}
