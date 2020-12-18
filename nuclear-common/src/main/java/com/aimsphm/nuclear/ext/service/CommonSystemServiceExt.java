package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.CommonSystemService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <系统信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonSystemServiceExt extends CommonSystemService {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSystemDO> listCommonSystemByPageWithParams(QueryBO<CommonSystemDO> queryBO);

    /**
     * 获取某系统信息结构树
     *
     * @param id
     * @return
     */
    TreeVO<Long, String> listCommonSystemTree(Long id);

}
