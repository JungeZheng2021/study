package com.aimsphm.nuclear.algorithm.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <算法的公共入参>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 14:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 14:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlgorithmParamDTO<T> implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 7824278330465676931L;
    /**
     * 算法类型
     */
    private String algorithmType;
    /**
     * 算法具体参数
     */
    private T data;
}
