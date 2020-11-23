package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.service.CommonSetService;

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
     * 获取某机组信息结构树
     *
     * @param id 机组id
     * @return
     */
    TreeVO<Long, String> listCommonSetTree(Long id);
}
