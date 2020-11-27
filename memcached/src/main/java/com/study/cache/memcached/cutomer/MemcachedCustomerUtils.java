package com.study.cache.memcached.cutomer;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Package: com.study.cache.memcached.cutomer
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/07/30 13:03
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/30 13:03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class MemcachedCustomerUtils {


    //如果多个memcached必须制定memcachedName
    private static SockIOPool sockIOPool = SockIOPool.getInstance("memcached1");

    //服务器列表 格式：127.0.0.1:port
    private static String[] servers = new String[]{"192.168.16.37:11211", "192.168.16.37:11212", "192.168.16.37:11213"};
    //服务器权重  所有权重的最大公约数应该是1  否则会造成资源浪费
    private static Integer[] serverWeights = new Integer[]{1, 1, 1};
    private static MemCachedClient cachedClient;

    static {

        //设置服务器列表
        sockIOPool.setServers(servers);

        //设置服务器的权重  权重和服务器的位置一一对应
        sockIOPool.setWeights(serverWeights);

        //设置开始时每个cache服务器的可用连接数
        sockIOPool.setInitConn(2);

        //设置每个服务器最少可用连接数
        sockIOPool.setMinConn(2);

        //设置每个服务器最大可用连接数
        sockIOPool.setMaxConn(10);

        //设置可用连接池的最长等待时间  ms
        sockIOPool.setMaxIdle(5000);

        /**
         *设置连接池维护线程的睡眠时间
         *设置为0，维护线程不启动
         *维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
         */
        sockIOPool.setMaintSleep(0);

        //设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）
        sockIOPool.setNagle(true);

        //设置socket的读取等待超时值 ms
        sockIOPool.setSocketTO(3000);

        //设置socket的连接等待超时值  ms
        sockIOPool.setSocketConnectTO(2000);

        /**
         *设置连接心跳监测开关。
         *设为true则每次通信都要进行连接是否有效的监测，造成通信次数倍增，加大网络负载，因此该参数应该在对HA要求比较高的场合设为TRUE，默认状态是false。
         */
        sockIOPool.setAliveCheck(false);

        /**
         *设置连接失败恢复开关
         *设置为TRUE，当宕机的服务器启动或中断的网络连接后，这个socket连接还可继续使用，否则将不再使用，默认状态是true，建议保持默认。
         */
        sockIOPool.setFailback(true);

        /**
         *设置容错开关
         *设置为TRUE，当当前socket不可用时，程序会自动查找可用连接并返回，否则返回NULL，默认状态是true，建议保持默认。
         */
        sockIOPool.setFailover(true);

        /**
         *设置hash算法
         *        alg=0 使用String.hashCode()获得hash code,该方法依赖JDK，可能和其他客户端不兼容，建议不使用
         *      alg=1 使用original 兼容hash算法，兼容其他客户端
         *       alg=2 使用CRC32兼容hash算法，兼容其他客户端，性能优于original算法
         *       alg=3 使用MD5 hash算法
         *采用前三种hash算法的时候，查找cache服务器使用余数方法。采用最后一种hash算法查找cache服务时使用consistent方法。
         */
        sockIOPool.setHashingAlg(3);

        //设置完pool参数后最后调用该方法，启动pool。
        sockIOPool.initialize();

        if (cachedClient == null) {

            cachedClient = new MemCachedClient("memcached1");

            /*设置cache数据的原始类型是String
                默认值是false
                只有在确定cache的数据类型是string的情况下才设为true，这样可以加快处理速度。
             */
            cachedClient.setPrimitiveAsString(false);

        }
    }

    private MemcachedCustomerUtils() {
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean set(String key, Object value) {
        return setExp(key, value, null);
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    public static boolean set(String key, Object value, Date expire) {
        return setExp(key, value, expire);
    }

    /**
     * 向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */

    private static boolean setExp(String key, Object value, Date expire) {

        boolean flag = false;
        try {
            flag = cachedClient.set(key, value, expire);
        } catch (Exception e) {
            // 记录Memcached日志
            log.error("Memcached set方法报错，key值：" + key + "\r\n");
        }
        return flag;
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean add(String key, Object value) {
        return addExp(key, value, null);
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    public static boolean add(String key, Object value, Date expire) {
        return addExp(key, value, expire);
    }

    /**
     * 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean addExp(String key, Object value, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.add(key, value, expire);
        } catch (Exception e) {
            // 记录Memcached日志
            log.error("Memcached add方法报错，key值：" + key + "\r\n");
        }
        return flag;
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean replace(String key, Object value) {
        return replaceExp(key, value, null);
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */

    public static boolean replace(String key, Object value, Date expire) {
        return replaceExp(key, value, expire);
    }

    /**
     * 仅当键已经存在时，replace 命令才会替换缓存中的键。
     *
     * @param key    键
     * @param value  值
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean replaceExp(String key, Object value, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.replace(key, value, expire);
        } catch (Exception e) {
            log.error("Memcached replace方法报错，key值：" + key + "\r\n");
        }
        return flag;
    }

    /**
     * get 命令用于检索与之前添加的键值对相关的值。
     *
     * @param key 键
     * @return
     */
    public static Object get(String key) {
        Object obj = null;
        try {
            obj = cachedClient.get(key);
        } catch (Exception e) {
            log.error("Memcached get方法报错，key值：" + key + "\r\n");
        }
        return obj;
    }

    /**
     * 删除 memcached 中的任何现有值。
     *
     * @param key 键
     * @return
     */
    public static boolean delete(String key) {
        return deleteExp(key, null);
    }

    /**
     * 删除 memcached 中的任何现有值。
     *
     * @param key    键
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    public static boolean delete(String key, Date expire) {
        return deleteExp(key, expire);
    }

    /**
     * 删除 memcached 中的任何现有值。
     *
     * @param key    键
     * @param expire 过期时间 New Date(1000*10)：十秒后过期
     * @return
     */
    private static boolean deleteExp(String key, Date expire) {
        boolean flag = false;
        try {
            flag = cachedClient.delete(key, expire);
        } catch (Exception e) {
            log.error("Memcached delete方法报错，key值：" + key + "\r\n");
        }
        return flag;
    }

    /**
     * 清理缓存中的所有键/值对
     *
     * @return
     */
    public static boolean flashAll() {
        boolean flag = false;
        try {
            flag = cachedClient.flushAll();
        } catch (Exception e) {
            log.error("Memcached flashAll方法报错\r\n");
        }
        return flag;
    }

    public static void main(String[] args) {
//        boolean add = MemcachedCustomerUtils.add("hello", "tom");
//        log.info("添加的结果：{}", add);
        Object obj = MemcachedCustomerUtils.get("hello");
        System.out.println("====" + obj + "====");
    }
}
