package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <调用算法>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmHandlerService<P, R> {
    Logger log = LoggerFactory.getLogger(AlgorithmHandlerService.class);

    /**
     * 获取调用算法结果-需要的组装类型-需要实现自己组装数据
     *
     * @param params 请求参数
     * @return
     */
    Object getInvokeCustomerData(P params);


    /**
     * 调用算法服务-可默认使用
     *
     * @param client 算法客户端
     * @param params 请求参数
     * @param type   算法类型
     * @param clazz  返回值类型
     * @return
     */
    default R invokeServer(AlgorithmServiceFeignClient client, P params, String type, Class<R> clazz) {
        try {
            Assert.notNull(client, "algorithm client is null");
            Object o = execute(client, params, type);
            String s1 = JSON.toJSONString(o);
            return JSON.parseObject(s1, clazz);
        } catch (Exception e) {
            log.error("data analysis failed...", e);
            throw new CustomMessageException("data analysis failed...", e);
        }
    }

    /**
     * 调用算法服务-可默认使用
     *
     * @param client       算法客户端
     * @param params       请求参数
     * @param type         算法类型
     * @param clazzInArray 返回值类型
     * @return
     */
    default List<R> invokeServerArray(AlgorithmServiceFeignClient client, P params, String type, Class<R> clazzInArray) {
        try {
            Assert.notNull(client, "algorithm client is null");
            Object o = execute(client, params, type);
            String s1 = JSON.toJSONString(o);
            return JSON.parseArray(s1, clazzInArray);
        } catch (Exception e) {
            log.error("data analysis failed...", e);
        }
        return null;
    }


    /**
     * 真正执行算法调用
     *
     * @param client 算法客户端
     * @param params 请求参数
     * @param type   算法类型
     * @return
     */
    default R execute(AlgorithmServiceFeignClient client, P params, String type) {
        try {
            AlgorithmParamDTO<P> query = new AlgorithmParamDTO();
            query.setData(params);
            query.setAlgorithmType(type);
            checkParams(query);
            log.debug("execute starting .....{}", JSON.toJSONString(query, SerializerFeature.WriteMapNullValue));
            ResponseData<R> responseData = client.algorithmInvokeByParams(query);
            checkSuccess(responseData);
            log.debug("<》：algorithm server success responseCode: {}, status: {}", responseData.getCode(), responseData.getMsg());
            return responseData.getData();
        } catch (Exception e) {
            log.error("algorithm server execute failed..{}", e);
        }
        return null;
    }


    /**
     * 校验调用算法是否成功
     *
     * @param response
     */
    default void checkSuccess(ResponseData<R> response) {
        Assert.isTrue(Objects.nonNull(response) && "200".equalsIgnoreCase(response.getCode()), "algorithm Invoke failed");
    }

    /**
     * 校验参数是否完整
     *
     * @param params
     */
    default void checkParams(AlgorithmParamDTO<P> params) {
        Assert.notNull(params.getAlgorithmType(), "algorithm type can not be null");
        Assert.notNull(params.getData(), "algorithm input data can not be null");
    }
}
