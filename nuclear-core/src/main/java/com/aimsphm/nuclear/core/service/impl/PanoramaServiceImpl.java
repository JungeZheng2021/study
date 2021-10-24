package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.mapper.JobAlarmEventMapper;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.core.entity.vo.PanoramaVO;
import com.aimsphm.nuclear.core.service.PanoramaService;
import com.aimsphm.nuclear.ext.service.MonitoringService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.CoreConstants.PANORAMA_ANOMALY;

/**
 * @Package: com.aimsphm.nuclear.core.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 10:40
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 10:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class PanoramaServiceImpl implements PanoramaService {

    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private MonitoringService monitoringService;

    @Resource
    private JobAlarmEventMapper eventMapper;

    @Override
    public List<PanoramaVO> getPanoramaDetails(Long subSystemId) {
        List<PanoramaVO> vo = Lists.newArrayList();
        LambdaQueryWrapper<CommonDeviceDO> wrapper = Wrappers.lambdaQuery(CommonDeviceDO.class);
        if (Objects.nonNull(subSystemId)) {
            wrapper.and(w -> w.in(CommonDeviceDO::getSubSystemId, subSystemId)).orderByAsc(CommonDeviceDO::getSort);
        } else {
            wrapper.and(w -> w.in(CommonDeviceDO::getSubSystemId, 1, 2)).orderByAsc(CommonDeviceDO::getSort);
        }
        List<CommonDeviceDO> list = deviceService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return vo;
        }
        return list.stream().map(this::getPanoramaVO).collect(Collectors.toList());
    }

    private PanoramaVO getPanoramaVO(CommonDeviceDO device) {
        PanoramaVO vo = new PanoramaVO();
        Long deviceId = device.getId();
        DeviceStatusVO status = monitoringService.getRunningStatus(device.getId());
        if (Objects.isNull(status)) {
            return vo;
        }
        BeanUtils.copyProperties(device, vo);
        BeanUtils.copyProperties(status, vo);
        //算法报警
        List<LabelVO> labelVOS = eventMapper.selectWarmingStatusPoints(deviceId);
        Map<Integer, Long> anomalyData = labelVOS.stream().collect(Collectors.toMap(x -> (Integer) x.getName(), x -> (Long) x.getValue()));
        PointCategoryEnum[] values = PointCategoryEnum.values();
        Map<Integer, String> items = Maps.newLinkedHashMap();
        for (PointCategoryEnum category : values) {
            Integer value = category.getValue();
            Long anomalyCounts = anomalyData.get(value);
            if (Objects.nonNull(anomalyCounts)) {
                items.put(category.getValue(), MessageFormat.format(PANORAMA_ANOMALY, anomalyCounts));
            }
        }
        vo.setItems(items);
        return vo;
    }
}
