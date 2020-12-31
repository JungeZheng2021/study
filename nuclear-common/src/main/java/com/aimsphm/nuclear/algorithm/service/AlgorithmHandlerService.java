package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.Assert;

import java.io.IOException;
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

    /**
     * 获取调用算法结果-需要的组装类型
     *
     * @param params 请求参数
     * @return
     */
    Object getInvokeCustomerData(P params);

    /**
     * 获取调用算法结果
     *
     * @param query 请求参数
     * @return
     */
    ResponseData<R> getInvokeServer(AlgorithmParamDTO<P> query);

    /**
     * 调用算法服务端
     *
     * @param params
     * @param type
     * @param clazz
     * @return
     */
    default R invokeServer(P params, String type, Class<R> clazz) {
        try {
            Object o = execute(params, type);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String s = mapper.writeValueAsString(o);
            return mapper.readValue(s, clazz);
        } catch (Exception e) {
            throw new CustomMessageException("算法结果异常");
        }
    }

    /**
     * 执行调用
     *
     * @param params
     * @param type
     * @return
     */
    default R execute(P params, String type) {
        AlgorithmParamDTO<P> query = new AlgorithmParamDTO();
        query.setData(params);
        query.setAlgorithmType(type);
        checkParams(query);
        ResponseData<R> responseData = getInvokeServer(query);
        checkSuccess(responseData);
        return responseData.getData();
    }


    /**
     * 校验调用算法是否成功
     *
     * @param response
     */
    default void checkSuccess(ResponseData<R> response) {
        Assert.isTrue(Objects.nonNull(response) && response.getCode().equalsIgnoreCase("200"), "调用算法失败");
    }

    /**
     * 校验参数是否完整
     *
     * @param params
     */
    default void checkParams(AlgorithmParamDTO<P> params) {
        Assert.notNull(params.getAlgorithmType(), "算法类型不能为空");
        Assert.notNull(params.getData(), "算法输入数据不能空");
    }
}
