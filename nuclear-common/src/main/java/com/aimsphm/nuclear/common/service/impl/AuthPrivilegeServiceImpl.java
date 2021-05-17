package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AuthPrivilegeDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.AuthPrivilegeMapper;
import com.aimsphm.nuclear.common.service.AuthPrivilegeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_PRIVILEGES;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <权限资源信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-05-06
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-05-06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AuthPrivilegeServiceImpl extends ServiceImpl<AuthPrivilegeMapper, AuthPrivilegeDO> implements AuthPrivilegeService {

    @Override
    public Page<AuthPrivilegeDO> listAuthPrivilegeByPageWithParams(QueryBO<AuthPrivilegeDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<AuthPrivilegeDO> customerConditions(QueryBO<AuthPrivilegeDO> queryBO) {
        LambdaQueryWrapper<AuthPrivilegeDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
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
    public List<AuthPrivilegeDO> listAuthPrivilege(String userAccount, String sysCode, String structured) {
        if (StringUtils.isEmpty(userAccount)) {
            throw new CustomMessageException("User not logged in");
        }
        if (StringUtils.isEmpty(sysCode)) {
            sysCode = "";
        }
        Set<String> privileges = getUserPrivilegeRest(userAccount, sysCode);
        if (CollectionUtils.isEmpty(privileges)) {
            return null;
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

    private List<AuthPrivilegeDO> operatePrivileges(List<AuthPrivilegeDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
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
                    List<Long> collect1 = value.stream().map(i -> i.getId()).collect(Collectors.toList());
                    needless.addAll(collect1);
                }
            });
        });
        List<AuthPrivilegeDO> collect1 = list.stream().filter(x -> !needless.contains(x.getId())).collect(Collectors.toList());
        return collect1;
    }

    @Value("${privileges-server-url:http://localhost/autogrant/privileges/queryUserQXPrivileges.so}")
    private String targetURL;

    /**
     * restTemplate请求
     *
     * @param userAccount
     * @param sysCode
     * @return
     */
    private Set<String> getUserPrivilegeRest(String userAccount, String sysCode) {
        Map<String, Object> data = new HashMap<>(16);
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        data.put("userAccount", userAccount);
        data.put("sysCode", sysCode);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(data, headers);
        ResponseEntity<String> entity = new RestTemplate().postForEntity(targetURL, request, String.class);

        int statusCodeValue = entity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            return null;
        }
        String body = entity.getBody();
        return privileges(body);
    }

    /**
     * 田湾请求方式
     *
     * @param userAccount
     * @param sysCode
     * @return
     */
    private Set<String> getUserPrivilege(String userAccount, String sysCode) {
        InputStream is = null;
        HttpURLConnection conn = null;
        try {

            URL url = new URL(targetURL);
            //打开和url之间的连接
            conn = (HttpURLConnection) url.openConnection();
            //请求方式
//            conn.setRequestMethod("POST");
            //设置请求编码
            conn.setRequestProperty("Charsert", "UTF-8");
            //设置通用的请求属性
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            String json = "{\"userAccount\":\"%s\",\"sysCode\":\"%s\"}";
//            String json = "userAccount=%s&sysCode=%s";
            String format = String.format(json, userAccount, sysCode);
            out.write(format.getBytes("UTF-8"));
            //缓冲数据
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            System.out.println(responseCode);
            if (responseCode == 200) {
                //获取URLConnection对象对应的输入流
                is = conn.getInputStream();
                //构造一个字符流缓存
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String lines;
                StringBuffer sb = new StringBuffer();
                while ((lines = reader.readLine()) != null) {
                    lines = new String(lines.getBytes(), "utf-8");
                    sb.append(lines);
                }
                return privileges(sb.toString());
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("get user privilege failed --> userAccount:{} , sysCode:{}, error:{}", userAccount, sysCode, e);
        } finally {
            //关闭流
            try {
                if (Objects.nonNull(is)) {

                    is.close();
                }
                if (Objects.nonNull(conn)) {
                    conn.disconnect();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    private Set<String> privileges(String sb) {
        JSONObject responseData = JSON.parseObject(sb);
        Integer result = responseData.getInteger("result");
        if (result != 1) {
            return null;
        }
        String message = responseData.getString("message");
        if (!StringUtils.hasText(message)) {
            return null;
        }

        return Arrays.stream(message.split(",")).collect(Collectors.toSet());
    }
}