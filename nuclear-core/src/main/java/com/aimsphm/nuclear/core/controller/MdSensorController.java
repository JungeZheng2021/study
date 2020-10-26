package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.enums.SensorTypeEnum;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.vo.GroupedMdSerdorVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("mdSensor")
@Api(tags = "传感器接口")
public class MdSensorController {

    @Autowired
    private MdSensorService iMdSensorService;
    @Autowired
    MdDeviceService iMdDeviceService;//added by Mao

    @PostMapping("list")
    @ApiOperation(value = "查询传感器列表接口")
    public Object getMdSensorList(@RequestBody QueryObject<MdSensor> queryObject) {
        QueryWrapper<MdSensor> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdSensorService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @PostMapping
    @ApiOperation(value = "新增传感器接口")
    public Object saveMdSensor(@RequestBody MdSensor MdSensor) {
        return ResponseUtils.success(iMdSensorService.save(MdSensor));
    }

    @PutMapping
    @ApiOperation(value = "修改传感器接口")
    public Object modifyMdSensor(@RequestBody MdSensor MdSensor) {
        return ResponseUtils.success(iMdSensorService.updateById(MdSensor));
    }

    @DeleteMapping
    @ApiOperation(value = "删除传感器接口")
    public Object delMdSensor(@RequestBody String ids) {
        return ResponseUtils.success(iMdSensorService.removeByIds(Arrays.asList(ids.split(","))));
    }

    @GetMapping("/getMdSensorBySubSystemId/{subSystemId}")
    @ApiOperation(value = "通过子系统id查询传感器列表")
    public Object getMdSensorBySubSystemId(@PathVariable(name = "subSystemId") Long subSystemId) {
        return ResponseUtils.success(iMdSensorService.getMdSensorBySubSystemId(subSystemId));
    }

    @GetMapping("/getGroupedMdSensorBySubSystemId/{subSystemId}")
    @ApiOperation(value = "通过子系统id查询分组后的传感器列表")
    public Object getGroupedMdSensorBySubSystemId(@PathVariable(name = "subSystemId") Long subSystemId) {
        List<MdSensor> resultList = iMdSensorService.getMdSensorBySubSystemId(subSystemId).stream().filter(x -> x.getClusterAnalysis() == true).collect(Collectors.toList());

        Map<Long, String> idDeviceNameMap = new HashMap<>(16);
        List<GroupedMdSerdorVO> groupedMdSerdorVOList = Lists.newArrayList();
        for (MdSensor sensor : resultList) {
            GroupedMdSerdorVO groupedMdSerdorVO = new GroupedMdSerdorVO();
            BeanUtils.copyProperties(sensor, groupedMdSerdorVO);
            Long deviceId = sensor.getDeviceId();
            if (idDeviceNameMap.containsKey(deviceId)) {
                groupedMdSerdorVO.setDeviceDisplayName(idDeviceNameMap.get(deviceId));
            } else {
                // MdDevice device = new MdDevice();
                //device.setId(deviceId);
                String deviceDispName = "";
                if (deviceId != null) {
                    deviceDispName = iMdDeviceService.getNameById(deviceId).split("_")[0];

                }
                idDeviceNameMap.put(deviceId, deviceDispName);
                groupedMdSerdorVO.setDeviceDisplayName(deviceDispName);
            }
            groupedMdSerdorVOList.add(groupedMdSerdorVO);
        }

        Map<String, List<GroupedMdSerdorVO>> groupedMdSensor = groupedMdSerdorVOList.stream()
                .collect(Collectors.groupingBy(GroupedMdSerdorVO::getSensorDesc));

        return ResponseUtils.success(groupedMdSensor);
    }

    //added by Mao


    @GetMapping("/getPublicMdSensorBySubSystemId/{subSystemId}")
    @ApiOperation(value = "通过子系统id查询共用的传感器列表")
    public Object getPublicMdSensorBySubSystemId(@PathVariable(name = "subSystemId") Long subSystemId) {
        return ResponseUtils.success(iMdSensorService.getPublicSendorBySubSystemId(subSystemId));
    }


    @GetMapping("list/{deviceId}/{location}")
    @ApiOperation(value = "振动分析-根据设备id获取振动分析测点")
    public Object getSensorByLocationCodeAndDeviceId(@PathVariable Long deviceId, @PathVariable String location) {
        List<MdSensor> sensors = iMdSensorService.getSensorByLocationCodeAndDeviceId(deviceId, location);
        return ResponseUtils.success(sensors);
    }


    @GetMapping("/getViFeatureMdSensor/{tagId}")
    @ApiOperation(value = "通过tag id查询该测点的特征属性")
    public Object getViFeatureMdSensor(@PathVariable(name = "tagId") String tagId) {
        return ResponseUtils.success(iMdSensorService.getViFeatureMdSensorMap(tagId));
    }

    @GetMapping("/getMdSensorExtraInfo/{tagId}")
    @ApiOperation(value = "通过tag id查询自装测点的详细信息")
    public Object getMdSensorExtraInfo(@PathVariable(name = "tagId") String tagId) {
        return ResponseUtils.success(iMdSensorService.getMdSensorExtraInfo(tagId));
    }

    @GetMapping("/isNonePiViSensor/{tagId}")
    @ApiOperation(value = "通过tag id是否是自装振动测点")
    public Object isNonePiViSensor(@PathVariable(name = "tagId") String tagId) {
        return ResponseUtils.success(iMdSensorService.isNonePiViSensor(tagId));
    }

    @GetMapping("/getAbNormalMdSensorExtraInfo")
    @ApiOperation(value = "通过subSystemId查询不正常的自装测点")
    public Object getAbNormalMdSensorExtraInfo(@RequestParam(name = "deviceId") Long deviceId, @RequestParam(name = "type") Integer type) {
        return ResponseUtils.success(iMdSensorService.getAbNormalMdSensorExtraInfo(deviceId, type));
    }

    @GetMapping("/getMdSensorWithNoPublicByDeviceId/{deviceId}")
    @ApiOperation(value = "通过设备id查询非共用的传感器列表")
    public Object getMdSensorWithNoPublicByDeviceId(@PathVariable(name = "deviceId") Long deviceId) {
        return ResponseUtils.success(iMdSensorService.getMdSensorsByDeviceId(deviceId).stream().filter(x -> x.getSystemSensor() == false).collect(Collectors.toList()));
    }

    @PostMapping("updateSensorWithTs")
    @ApiOperation(value = "更新传感器")
    public Object updateSensorWithTs(@RequestBody MdSensor sensor) {
        try {
            iMdSensorService.updateWithOptismicLock(sensor);
        } catch (Exception ex) {
            return ResponseUtils.error(ex.getMessage());
        }
        return ResponseUtils.success("The Sensor Record has been updated");
    }

    @GetMapping("/getGroupedMdSensorByTypeAndSubsystemId/{subSystemId}")
    @ApiOperation(value = "通过子系统id查询分好组的传感器，同时把类型区分开")
    public Object getGroupedMdSensorByTypeAndSubsystemId(@PathVariable(name = "subSystemId") Long subSystemId) {
        List<MdSensor> resultList = iMdSensorService.getMdSensorBySubSystemId(subSystemId).stream().filter(x -> x.getClusterAnalysis() == true ).collect(Collectors.toList());
        Map<Long, String> idDeviceNameMap = new LinkedHashMap<>();
        List<GroupedMdSerdorVO> groupedMdSerdorVOList = Lists.newArrayList();
        for (MdSensor sensor : resultList) {
            GroupedMdSerdorVO groupedMdSerdorVO = new GroupedMdSerdorVO();
            BeanUtils.copyProperties(sensor, groupedMdSerdorVO);
            Long deviceId = sensor.getDeviceId();
            if (idDeviceNameMap.containsKey(deviceId)) {
                groupedMdSerdorVO.setDeviceDisplayName(idDeviceNameMap.get(deviceId));
            } else {
                // MdDevice device = new MdDevice();
                //device.setId(deviceId);
                String deviceDispName = "";
                if (deviceId != null) {
                    deviceDispName = iMdDeviceService.getNameById(deviceId).split("_")[0];

                }
                idDeviceNameMap.put(deviceId, deviceDispName);
                groupedMdSerdorVO.setDeviceDisplayName(deviceDispName);
            }
            groupedMdSerdorVOList.add(groupedMdSerdorVO);
        }
        Map<Integer, List<GroupedMdSerdorVO>> groupedMdSensorByType = groupedMdSerdorVOList.stream()
                .collect(Collectors.groupingBy(GroupedMdSerdorVO::getSensorType));
        Map<String, Map<String, List<GroupedMdSerdorVO>>> returnMap = new LinkedHashMap<>();

        for (Integer type : groupedMdSensorByType.keySet()) {
            String sectionName = SensorTypeEnum.getDesc(type.byteValue());
            List<GroupedMdSerdorVO> tempList = groupedMdSensorByType.get(type);
            Map<String, List<GroupedMdSerdorVO>> tempMap = tempList.stream()
                    .collect(Collectors.groupingBy(GroupedMdSerdorVO::getSensorDesc));

            Map<String, List<GroupedMdSerdorVO>> sortedTempMap = new LinkedHashMap<>();
            tempMap.entrySet().stream().sorted(Comparator.comparing(x -> x.getKey())).forEachOrdered(e -> sortedTempMap.put(e.getKey(), e.getValue()));

            returnMap.put(sectionName, sortedTempMap);

        }
        return ResponseUtils.success(returnMap);
    }
}
