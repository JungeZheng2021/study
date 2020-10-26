package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 *
 * @author Mao
 * @since 2020-05-11
 */
public interface MdRuntimeBaseCfgService  extends IService<MdRuntimeBaseCfg>{

    @Transactional
    Boolean updateConfigBySubSystemId(List<MdRuntimeBaseCfg> configList) throws Exception;

    public Boolean updateConfigByDeviceId(List<MdRuntimeBaseCfg> configList) throws Exception;
    public Boolean updateSingleConfig(MdRuntimeBaseCfg config) throws Exception;

    public List<MdRuntimeBaseCfg> getAllConfigByDeviceId(Long deviceId);


    List<MdRuntimeBaseCfg> getCreateAllConfigBySubSystemId(Long subSystemId);
}