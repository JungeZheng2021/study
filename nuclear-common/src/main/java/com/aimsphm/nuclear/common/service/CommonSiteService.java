package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSiteDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-17 14:30
 */
public interface CommonSiteService extends IService<CommonSiteDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<CommonSiteDO> listCommonSiteByPageWithParams(QueryBO<CommonSiteDO> queryBO);

    /**
     * 获取电厂信息结构树
     *
     * @param id 电厂id
     * @return 对象
     */
    TreeVO<Long, String> listCommonSetTree(Long id);
}
