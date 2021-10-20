package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AuthPrivilegeDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-05-06 14:30
 */
public interface AuthPrivilegeService extends IService<AuthPrivilegeDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AuthPrivilegeDO> listAuthPrivilegeByPageWithParams(QueryBO<AuthPrivilegeDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AuthPrivilegeDO> listAuthPrivilegeWithParams(QueryBO<AuthPrivilegeDO> queryBO);

    /**
     * 获取资源数据
     *
     * @return 集合
     */
    List<AuthPrivilegeDO> listAuthPrivilege();

    /**
     * 根据用户名和系统号查询用户权限
     *
     * @param username 用户名
     * @param sysCode  系统code
     * @return 集合
     */
    List<AuthPrivilegeDO> listAuthPrivilege(String username, String sysCode, String structured);

    /**
     * 根据username获取userid
     *
     * @param username 用户名称
     * @return 字符串
     */
    String getUserIdByUsername(String username);
}
