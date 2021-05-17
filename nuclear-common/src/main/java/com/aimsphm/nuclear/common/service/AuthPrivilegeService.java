package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AuthPrivilegeDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <权限资源信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-05-06
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-05-06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AuthPrivilegeService extends IService<AuthPrivilegeDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AuthPrivilegeDO> listAuthPrivilegeByPageWithParams(QueryBO<AuthPrivilegeDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AuthPrivilegeDO> listAuthPrivilegeWithParams(QueryBO<AuthPrivilegeDO> queryBO);

    /**
     * 获取资源数据
     *
     * @return
     */
    List<AuthPrivilegeDO> listAuthPrivilege();

    /**
     * 根据用户名和系统号查询用户权限
     *
     * @param userAccount
     * @param sysCode
     * @return
     */
    List<AuthPrivilegeDO> listAuthPrivilege(String userAccount, String sysCode, String structured);
}
