package com.study.cache.redis.a0_example;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("single") // 设置profile
@Slf4j
public class JedisTests {

    @Test
    public void comm() {
        Jedis jedis = init();

        Long status = jedis.del("name");
        log.info("删除的对应key的个数：{}", status);

        byte[] dump = jedis.dump("name");
        log.info("序列化对应的key的值：{}", dump);

        Boolean exists = jedis.exists("name");
        log.info("判定指定的key是否存在：{}", exists);

        Long name = jedis.expire("name", 1000);
        log.info("给指定的可以设置过期时间：{}", name);

        Long aliveTime = jedis.ttl("name");
        log.info("剩余存活时间：{}", aliveTime);

        String type = jedis.type("name");
        log.info("给定key值的类型：{}", type);
    }

    private Jedis init() {
        JedisShardInfo config = new JedisShardInfo("192.168.16.28", 6379);
        config.setPassword("aims2016");
        return new Jedis(config);
    }

    @Test
    public void stringType() {
        Jedis jedis = init();

        //设置key和value -返回是否设置状态OK
        String status = jedis.set("name", "milla");
        log.info("返回的结果：{}" + status);
        //获取数据
        String name = jedis.get("name");
        log.info("取出key的值：{}" + name);
        //将对应的key数值加一
        Long add = jedis.incr("count");
        log.info("加一的结果：{}" + add);
        //将对应的key数值减一
        Long sub = jedis.decr("count");
        log.info("减一的结果：{}" + sub);
        //获取多个key 的值
        List<String> mValues = jedis.mget("name", "count", "empty");
        //如果对应的key没有值会返回一个null元素
        log.info("多个key的结果：{}" + mValues);
    }

    // ------------------------ jedis 工具直连演示
    // jedis和redis命令名称匹配度最高，最为简洁，学习难度最低

    // 列表~ 集合数据存储~ java.util.List，java.util.Stack
    // 生产者消费者（简单MQ）
    @Test
    public void listType() {
        Jedis jedis = init();

        // 插入数据1 --- 2 --- 3
        Long list = jedis.rpush("list", "1", "2", "3");
        log.info("返回值，成功插入数据的个数：{}", list);
        jedis.lpush("list", "0");
        jedis.rpush("list", "4");
        //获取一个或者多个值，指定开始和结束索引
        List<String> strings = jedis.lrange("list", 0, -1);
        log.info("获取指定key中所有的元素：{}", strings);
        //移除列表中第一个元素
        String start = jedis.lpop("list");
        log.info("移除第一个元素是：{}", start);
        //移除列表中最后一个元素
        String last = jedis.rpop("list");
        log.info("移除最后一个元素是：{}", last);

        jedis.ltrim("list", 0, 1);
        List<String> trim = jedis.lrange("list", 0, -1);
        log.info("获取trim之后的list：{}", trim);
    }

    // 类似：在redis里面存储一个hashmap
    // 推荐的方式，无特殊需求是，一般的缓存都用这个
    @Test
    public void hashType() {
        Jedis jedis = init();
        jedis.hset("user", "name", "tom");
        jedis.hset("user", "age", "18");
        jedis.hset("user", "userId", "10001");
        String name = jedis.hget("user", "name");
        log.info("获取用户姓名：{}", name);
        List<String> hmget = jedis.hmget("user", "name", "age");
        log.info("获取key下面的多个属性值：{}", hmget);
        Map<String, String> userMap = jedis.hgetAll("user");
        log.info("获取用户信息列表：{}", userMap);
    }

    @Test
    public void geoType() {
        Jedis jedis = init();
        jedis.geoadd("test", 20.3, 56.9, "拱宸桥");
        jedis.geoadd("test", 80.3, 16.9, "滨和地铁站");

        Double geodist = jedis.geodist("test", "拱宸桥", "滨和地铁站");
        log.info("获取两个地理位置的距离: {}", geodist);

        List<String> geohash = jedis.geohash("test", "拱宸桥");
        log.info("获取一/多个地理位置的geoHash集合: {}", geohash);

        List<GeoCoordinate> geopos = jedis.geopos("test", "拱宸桥", "滨和地铁站");
        log.info("获取一/多个地理位置的地理位置集合: {}", geopos);

        //类似与画一个圆，圆内所有的房源信息等等
        List<GeoRadiusResponse> radiusList = jedis.georadius("test", 21.3, 50.2, 200000, GeoUnit.KM);
        log.info("根据给定地理位置坐标获取指定范围内的地理位置集合: {}", radiusList);

        List<GeoRadiusResponse> geoRadiusResponses = jedis.georadiusByMember("test", "滨和地铁站", 9000000, GeoUnit.KM);
        log.info("根据指定成员的位置获取至额定范围内的位置信息集合: {}", geoRadiusResponses);
    }

    // 用set实现（交集 并集）
    // 交集示例： 共同关注的好友
    // 并集示例：
    @Test
    public void setType() {
        // 取出两个人共同关注的好友
        Jedis jedis = init();

        // 每个人维护一个set
        jedis.sadd("news1", "userC", "userD", "userE");
        jedis.sadd("news2", "userC", "userE", "userF");
        // 取出共同关注
        Set<String> sinter = jedis.sinter("news1", "news2");
        log.info("关注新闻1和新闻2的用户： {}", sinter);

        // 检索给新闻点赞/转发的
        jedis.sadd("news_zan", "userC", "userD", "userE");
        jedis.sadd("news_zhuan", "userE", "userF");
        // 取出共同人群
        Set<String> union = jedis.sunion("news_zan", "news_zhuan");
        log.info("关注某新闻(点赞和转发)的所有人：{}", union);

        Boolean sismember = jedis.sismember("new1", "userC");
        log.info("指定key的集合中是否包含某元素：{}", sismember);

        Set<String> members = jedis.smembers("news1");
        log.info("关注新闻1的所有的用户：{}", members);
        String member = jedis.spop("news1");
        log.info("随机移除一个关注新闻1的用户：{}", member);

        Long count = jedis.scard("new1");
        log.info("获取集合中成员的数量：{}", count);
    }

    // 游戏排行榜
    @Test
    public void sortedSetType() {
        Jedis jedis = init();
        String ranksKeyName = "exam_rank";
        jedis.zadd(ranksKeyName, 100.0, "milla");
        jedis.zadd(ranksKeyName, 82.0, "lilei");
        jedis.zadd(ranksKeyName, 90, "tom");
        jedis.zadd(ranksKeyName, 96, "lily");
        jedis.zadd(ranksKeyName, 89, "jerry");

        Set<String> stringSet = jedis.zrevrange(ranksKeyName, 0, 2);
        log.info("返回前三名:{}", stringSet);

        Long zcount = jedis.zcount(ranksKeyName, 85, 100);
        log.info("超过85分的数量:{} ", zcount);

        Long zcard = jedis.zcard(ranksKeyName);
        log.info("获取成员的数量:{} ", zcard);
        //移除tom
        Long zrem = jedis.zrem(ranksKeyName, "tom");
        log.info("移除一个成员:{} ", zrem);
        //获取所有的成员，指定起止索引 -1代表到最后
        Set<String> zrange = jedis.zrange(ranksKeyName, 0, -1);
        log.info("移除一个成员:{} ", zrange);
    }
}

