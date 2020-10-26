package com.aimsphm.nuclear.pump.controller;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.TxRotatingsnapshot;
import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.mapper.MdSensorMapper;
import com.aimsphm.nuclear.common.mapper.TxPumpsnapshotMapper;
import com.aimsphm.nuclear.common.mapper.TxRotatingsnapshotMapper;
import com.aimsphm.nuclear.common.mapper.TxTurbinesnapshotMapper;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.aimsphm.nuclear.core.feign.PythonServerFeign;
import com.aimsphm.nuclear.core.feign.TornadoFeign;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/26 13:55
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/26 13:55
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "数据操作接口-以后会删除")
@RequestMapping(value = "test", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisTool {


    @Autowired
    private TxPumpsnapshotMapper deviceMapper;
    @Autowired
    private TxTurbinesnapshotMapper tbMapper;
    @Autowired
    private TxRotatingsnapshotMapper rotatingMapper;
    @Autowired
    private HotSpotDataService redis;

    @Autowired
    private TornadoFeign tornadoFeign;
    @Autowired
    private PythonServerFeign serverFeign;


    @GetMapping("tornado/test/{number}")
    @ApiOperation(value = "Test tornado")
    public Object testTornado(@PathVariable Integer number) {
        Object o = tornadoFeign.testTornado(number);
        return o;
    }

    @PostMapping("tornado/server")
    @ApiOperation(value = "Test tornado eureka")
    public Object serverFeign(@RequestBody Object obj) {
        Object o = serverFeign.health(obj);
        return o;
    }

    @GetMapping("pump/{deviceId}")
    @ApiOperation(value = "增加主泵设备")
    public Object pump(@PathVariable Long deviceId) {
        TxPumpsnapshot snapshot = deviceMapper.selectById(deviceId);
        redis.setPumpSnapshot(snapshot);
        return ResponseUtils.success(snapshot);
    }

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> template;

    @GetMapping("turbine/{deviceId}")
    @ApiOperation(value = "增加汽机设备")
    public Object turbine(@PathVariable Long deviceId) {
        TxTurbinesnapshot snapshot = tbMapper.selectById(deviceId);
        template.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + snapshot.getDeviceId(), snapshot);
        return ResponseUtils.success(snapshot);
    }

    @GetMapping("rotating/{deviceId}")
    @ApiOperation(value = "增加旋机设备")
    public Object rotating(@PathVariable Long deviceId) {
        LambdaQueryWrapper<TxRotatingsnapshot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TxRotatingsnapshot::getDeviceId, deviceId).last(" limit 1");
        TxRotatingsnapshot snapshot = rotatingMapper.selectOne(wrapper);
        template.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + snapshot.getDeviceId(), snapshot);
        return ResponseUtils.success(snapshot);
    }


    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("delete/{pre}")
    @ApiOperation(value = "根据key进行删除", notes = "根据前缀进行批量删除-后期删除")
    public Long delete(@PathVariable String pre) {
        Set<String> keys = redisTemplate.keys(pre + "*");
        return redisTemplate.delete(keys);
    }

    @GetMapping("deleteBy/{key}")
    @ApiOperation(value = "根据key进行删除", notes = "根据前缀进行批量删除-后期删除")
    public Boolean deleteByKey(@PathVariable String key) {
        return redisTemplate.delete(key);
    }

    @GetMapping("points/{itemId}")
    @ApiOperation(value = "根据测点获取测点的实时显示信息-删除", notes = "根据前缀进行批量删除-后期删除")
    public Object get(@PathVariable String itemId) {
        return redisTemplate.opsForValue().get(CommonConstant.REDIS_POINT_REAL_TIME_PRE + itemId);
    }

    @GetMapping("{key}")
    @ApiOperation(value = "根据key获取对应redis值", notes = "根据前缀进行批量删除-后期删除")
    public Object getByKey(@PathVariable String key) {
        return redisTemplate.opsForValue().get(key);
    }


    MdSensorMapper sensorMapper;

    @PostMapping("{key}")
    @ApiOperation(value = "增加key的值", notes = "根据前缀进行批量删除-后期删除")
    public Boolean postKey(@PathVariable String key, @RequestBody MeasurePointVO object) {
        redisTemplate.opsForValue().set(key, object);
        return true;
    }
}
