package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AuthPrivilegeDO;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.AuthPrivilegeMapper;
import com.aimsphm.nuclear.common.service.AuthPrivilegeService;
import com.aimsphm.nuclear.common.util.AuthRequestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_PRIVILEGES;

/**
 * <p>
 * 功能描述:权限资源信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-05-06 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AuthPrivilegeServiceImpl extends ServiceImpl<AuthPrivilegeMapper, AuthPrivilegeDO> implements AuthPrivilegeService {
    @Resource
    private AuthRequestUtils authUtils;


    @Override
    public Page<AuthPrivilegeDO> listAuthPrivilegeByPageWithParams(QueryBO<AuthPrivilegeDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     * DateUtils
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<AuthPrivilegeDO> customerConditions(QueryBO<AuthPrivilegeDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<AuthPrivilegeDO> listAuthPrivilegeWithParams(QueryBO<AuthPrivilegeDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    @Cacheable(value = REDIS_KEY_PRIVILEGES)
    public List<AuthPrivilegeDO> listAuthPrivilege() {
        LambdaQueryWrapper<AuthPrivilegeDO> wrapper = Wrappers.lambdaQuery(AuthPrivilegeDO.class);
        //根据sort字段排序
        wrapper.orderByAsc(AuthPrivilegeDO::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<AuthPrivilegeDO> listAuthPrivilege(String username, String sysCode, String structured) {
        if (StringUtils.isEmpty(username)) {
            throw new CustomMessageException("User not logged in");
        }
        Set<String> privileges = authUtils.getUserPrivilegeByUsername(username, sysCode);
        if (CollectionUtils.isEmpty(privileges)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<AuthPrivilegeDO> wrapper = Wrappers.lambdaQuery(AuthPrivilegeDO.class);
        wrapper.in(AuthPrivilegeDO::getCode, privileges);
        wrapper.orderByAsc(AuthPrivilegeDO::getSort);
        if (StringUtils.hasText(structured)) {
            //数据权限
            wrapper.in(AuthPrivilegeDO::getCategory, 0);
            return this.list(wrapper);
        }
        //资源权限
        wrapper.in(AuthPrivilegeDO::getCategory, 1);
        List<AuthPrivilegeDO> list = this.list(wrapper);
        return operatePrivileges(list);
    }

    @Override
    public String getUserIdByUsername(String username) {
        return authUtils.getUserIdByUserName(username);
    }

    private List<AuthPrivilegeDO> operatePrivileges(List<AuthPrivilegeDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Map<Long, List<AuthPrivilegeDO>> collect = list.stream().collect(Collectors.groupingBy(AuthPrivilegeDO::getParentId, TreeMap::new, Collectors.toList()));
        Set<Long> needless = new HashSet<>();
        collect.entrySet().stream().forEach(x -> {
            Long key = x.getKey();
            list.stream().forEach(m -> {
                Long id = m.getId();
                if (key.equals(id)) {
                    List<AuthPrivilegeDO> value = x.getValue();
                    m.setChildren(value);
                    List<Long> collect1 = value.stream().map(BaseDO::getId).collect(Collectors.toList());
                    needless.addAll(collect1);
                }
            });
        });
        return list.stream().filter(x -> !needless.contains(x.getId())).collect(Collectors.toList());
    }
}