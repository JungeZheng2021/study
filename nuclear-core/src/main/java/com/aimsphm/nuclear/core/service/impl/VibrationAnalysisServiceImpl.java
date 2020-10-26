package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.core.service.VibrationAnalysisService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.core.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/14 14:40
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/14 14:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class VibrationAnalysisServiceImpl implements VibrationAnalysisService {
    @Autowired
    private MdSensorService sensorService;

    @Override
    public Map<String, String> listSensorLocation(Long deviceId) {
        List<MdSensor> points = sensorService.getSensorByLocationCodeAndDeviceId(deviceId, null);
        return operationLocations(points);
    }

    private Map<String, String> operationLocations(List<MdSensor> points) {
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        Map<String, String> collect = points.stream().filter(item -> Objects.nonNull(item) && StringUtils.hasText(item.getSensorDes()) && item.getSensorDes().length() > 2)
                .collect(Collectors.toMap(item -> item.getSensorDes().substring(0, 2), vo ->
                        vo.getSensorDesc().length() > 4 ? vo.getSensorDesc().substring(0, vo.getSensorDesc().length() - 4) : org.apache.commons.lang3.StringUtils.EMPTY, (a, b) -> a));
        return collect;
    }

    @Override
    public Map<String, String> listSensorLocationBySubSystemId(Long subSystemId) {
        List<MdSensor> points = sensorService.getSensorBySubSystemId(subSystemId);
        return operationLocations(points);
    }
}
