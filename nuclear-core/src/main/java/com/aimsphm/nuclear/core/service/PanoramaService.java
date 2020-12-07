package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.core.entity.vo.PanoramaVO;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <系统总览服务类>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 10:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 10:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface PanoramaService {

    /**
     * 根据子系统id获取总览信息
     *
     * @param subSystemId
     * @return
     */
    List<PanoramaVO> getPanoramaDetails(Long subSystemId);
}
