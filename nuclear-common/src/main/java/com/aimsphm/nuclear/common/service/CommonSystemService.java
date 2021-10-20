package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSystemDO;
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
public interface CommonSystemService extends IService<CommonSystemDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<CommonSystemDO> listCommonSystemByPageWithParams(QueryBO<CommonSystemDO> queryBO);

    /**
     * 获取某系统信息结构树
     *
     * @param id id
     * @return 对象
     */
    TreeVO<Long, String> listCommonSystemTree(Long id);

}
