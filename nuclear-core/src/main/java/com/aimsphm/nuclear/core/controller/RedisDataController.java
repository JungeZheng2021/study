package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.ext.service.MonitoringService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.*;

/**
 * <p>
 * 功能描述:Redis数据操作类-开发使用
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/3 9:35
 */
@RestController()
@Api(tags = "AAA-Redis数据操作类-开发使用")
@RequestMapping(value = "redis", produces = MediaType.APPLICATION_JSON_VALUE)
public class RedisDataController {
    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("")
    @ApiOperation(value = "更新redis所有测点数据")
    public List<MeasurePointVO> updatePointsData(boolean defaultValue, CommonQueryBO queryBO) {
        return monitoringService.updatePointsData(defaultValue, queryBO);
    }

    @GetMapping("clear")
    @ApiOperation(value = "清空query_cache开头缓存信息", notes = "query_cache开头一般为业务缓存数据(删除后会重新查询)")
    public Long removeAllData() {
        Set<String> keys = client.keys(CACHE_KEY_PREFIX + "*");
        return client.delete(keys);
    }

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @PostMapping("data/{key}")
    @ApiOperation(value = "增加key的值-指定对象增加", notes = "后期删除")
    public Object postKey(@PathVariable String key, @RequestBody Object object) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(object));
        stringRedisTemplate.opsForValue().set(key + "_str", JSON.toJSONString(object));
        return object;
    }

    @GetMapping("data/{key}")
    @ApiOperation(value = "增加key的值-指定对象增加", notes = "后期删除")
    public Map<String, Object> postKey(@PathVariable String key) {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put(key, redisTemplate.opsForValue().get(key));
        map.put(key + "_str", stringRedisTemplate.opsForValue().get(key + "_str"));
        return map;
    }

    @GetMapping("delete/{pre}")
    @ApiOperation(value = "根据key前缀进行删除", notes = "后期删除")
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

    @GetMapping("queue/{pointId}")
    @ApiOperation(value = "查询波形队列数据", notes = "后期删除")
    public List queue(@PathVariable Long pointId) {
        return redisTemplate.opsForList().range(REDIS_QUEUE_REAL_TIME_PRE + pointId, 0, -1);
    }

    @GetMapping("queue/remove")
    @ApiOperation(value = "删除波形队列数据", notes = "传pointId是指定删除，不传的话是删除所有波形数据")
    public Long queueRemove(Long id) {
        if (Objects.nonNull(id)) {
            return redisTemplate.delete(REDIS_QUEUE_REAL_TIME_PRE + id) ? 1L : 0L;
        }
        Set<String> keys = redisTemplate.keys(REDIS_QUEUE_REAL_TIME_PRE + "*");
        return redisTemplate.delete(keys);
    }

}
