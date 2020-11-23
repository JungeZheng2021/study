package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.core.service.MonitoringService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_POINT_REAL_TIME_PRE;

/**
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <Redis数据操作类-开发使用>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "AAA-Redis数据操作类-开发使用")
@RequestMapping(value = "redis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisDataController {
    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("")
    @ApiOperation(value = "更新redis所有测点数据")
    public List<CommonMeasurePointDO> updatePointsData() {
        return monitoringService.updatePointsData();
    }

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisClient client;

    @GetMapping("list/{pre}")
    @ApiOperation(value = "根据key前缀获取数据", notes = "后期删除")
    public List<Object> listPre(@PathVariable String pre) {
        Set<String> keys = client.keys(pre + "*");
        return client.multiGet(keys);
    }

    @GetMapping("{key}")
    @ApiOperation(value = "根据key获取对应redis值", notes = "后期删除")
    public Object getByKey(@PathVariable String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @PostMapping("{key}")
    @ApiOperation(value = "增加key的值", notes = "后期删除")
    public Boolean postKey(@PathVariable String key, @RequestBody MeasurePointVO object) {
        redisTemplate.opsForValue().set(key, object);
        return true;
    }

    @GetMapping("delete/{pre}")
    @ApiOperation(value = "根据key进行删除", notes = "后期删除")
    public Long delete(@PathVariable String pre) {
        Set<String> keys = redisTemplate.keys(pre + "*");
        return redisTemplate.delete(keys);
    }


    @GetMapping("deleteBy/{key}")
    @ApiOperation(value = "根据key进行删除", notes = "后期删除")
    public Boolean deleteByKey(@PathVariable String key) {
        return redisTemplate.delete(key);
    }

    @GetMapping("points/{itemId}")
    @ApiOperation(value = "根据测点获取测点的实时显示信息-删除", notes = "后期删除")
    public Object get(@PathVariable String itemId) {
        return redisTemplate.opsForValue().get(REDIS_POINT_REAL_TIME_PRE + itemId);
    }
}
