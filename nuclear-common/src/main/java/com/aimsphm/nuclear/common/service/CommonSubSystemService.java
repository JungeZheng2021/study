package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <子系统信息扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonSubSystemService extends IService<CommonSubSystemDO> {
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<CommonSubSystemDO> listCommonSubSystemByPageWithParams(QueryBO<CommonSubSystemDO> queryBO);

    /**
     * 获取子系统信息结构树
     *
     * @param id 子系统id
     * @return
     */
    TreeVO<Long, String> listCommonSubSystemTree(Long id);
}
