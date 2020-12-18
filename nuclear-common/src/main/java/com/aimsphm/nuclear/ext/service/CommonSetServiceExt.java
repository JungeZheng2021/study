package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSetDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.CommonSetService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <机组信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonSetServiceExt extends CommonSetService {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSetDO> listCommonSetByPageWithParams(QueryBO<CommonSetDO> queryBO);

    /**
     * 获取某机组信息结构树
     *
     * @param id 机组id
     * @return
     */
    TreeVO<Long, String> listCommonSetTree(Long id);
}
