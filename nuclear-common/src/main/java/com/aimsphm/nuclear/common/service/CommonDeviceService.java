package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
public interface CommonDeviceService extends IService<CommonDeviceDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonDeviceDO> listCommonDeviceByPageWithParams(QueryBO<CommonDeviceDO> queryBO);

    /**
     * 获取数据列表
     *
     * @param queryBO 查询条件
     * @return
     */
    List<CommonDeviceDO> listCommonDeviceWithParams(QueryBO<CommonDeviceDO> queryBO);

}
