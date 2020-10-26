package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdDeviceDetails;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备详细信息
 *
 * @author MILLA
 * @since 2020-06-10
 */
public interface MdDeviceDetailsService extends IService<MdDeviceDetails> {

    List<MdDeviceDetails> listDeviceInfo(Long deviceId);

    List<MdDeviceDetails> listDeviceInfo(MdDeviceDetails query);

    /**
     * 获取详细信息中的上次启动时间和上次停机时间
     *
     * @param deviceId
     * @return
     */
    Map<String, Date> getLatTimes(Long deviceId);
}