package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.mapper.SparkDownSampleMapper;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <降采样扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class SparkDownSampleServiceImpl extends ServiceImpl<SparkDownSampleMapper, SparkDownSample> implements SparkDownSampleService {

}
