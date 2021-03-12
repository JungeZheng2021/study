package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <算法配置服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizJobQuartzConfigService extends IService<BizJobQuartzConfigDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<BizJobQuartzConfigDO> listBizJobQuartzConfigByPageWithParams(QueryBO<BizJobQuartzConfigDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<BizJobQuartzConfigDO> listBizJobQuartzConfigWithParams(QueryBO<BizJobQuartzConfigDO> queryBO);
}
