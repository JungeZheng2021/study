package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <设备信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonDeviceServiceExt extends CommonDeviceService {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonDeviceDO> listCommonDeviceByPageWithParams(QueryBO<CommonDeviceDO> queryBO);
}
