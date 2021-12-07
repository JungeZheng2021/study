package com.study.mq.service.impl;

import com.study.mq.service.IOrderService;
import com.study.mq.util.CacheResourcesUtil;
import com.study.mq.util.ThreadLocalStore;
import com.study.mq.util.entity.DataQuery;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.example.mq.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/05 09:50
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/05 09:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class OrderServiceImpl implements IOrderService {

    private CacheResourcesUtil cache = new CacheResourcesUtil();

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private RabbitTemplate template;
    private final RestTemplate restTemplate = new RestTemplate();
    static final Map<String, Integer> count = new HashMap();
    static volatile Map<String, Boolean> enable = new HashMap();

    List<String> objects = Lists.newArrayList(new String[]{"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1", "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5", "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3", "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24"});

    public OrderServiceImpl() {
        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(() -> {
            cache.delayClear();
            //1个小时清除一次缓存
        }, 10000, 5 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public int saveOrder() {
        this.saveOrderLocal();
        log.info("2.推送订单消息到MQ....");
        String message = UUID.randomUUID().toString();
        this.template.convertAndSend("", "orderQueue", message);
        log.info("2.1.发送数据成功:message:{}", message);
        log.info("3.回调函数接收MQ的推送结果....");
        return 0;
    }

    @Async
    @Override
    public void enable(DataQuery query) {
        cache.setParams(query);
        this.saveOrder();
        enable.put(query.getUrl(), true);
        while (enable.get(query.getUrl())) {
            List<String> resources = cache.cache(query.getUrl());
            if (CollectionUtils.isEmpty(resources)) {
                continue;
            }
            try {
                Random random = new Random();
                int i = random.nextInt(this.objects.size());
                int urlIndex = random.nextInt(resources.size());
                HttpHeaders requestHeaders = new HttpHeaders();
                if (i < this.objects.size()) {
                    requestHeaders.add("User-Agent", this.objects.get(i));
                }
                String url = resources.get(urlIndex == resources.size() ? urlIndex - 1 : urlIndex);
                HttpEntity<Object> entity = new HttpEntity(null, requestHeaders);
                ResponseEntity<String> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class, new Object[0]);
                String id = url.substring(url.lastIndexOf("/") + 1);
                count.put(id, Objects.isNull(count.get(id)) ? 1 : count.get(id) + 1);
                log.info("topic:{}\t\t特征值:{}\t\t接收次数:{}\t状态:{}", id, cache.get(id), count.get(id), response.getStatusCodeValue());
                Thread.sleep(Objects.nonNull(query.getSleepTime()) ? query.getSleepTime() : (long) (new Random()).nextInt(20000));
            } catch (RestClientException var16) {
                var16.printStackTrace();
            } catch (InterruptedException var17) {
                var17.printStackTrace();
            } finally {
            }
        }
        log.info("退出执行");
    }

    @Override
    public void disabled(String url) {
        enable.put(url, false);
        count.clear();
        ThreadLocalStore.clear();
    }

    @Transactional(rollbackFor = {})
    public void saveOrderLocal() {
        log.info("1.1.生成订单....");
        log.info("1.2.生成需要推送记录....");
    }

    public static void main(String[] args) {
        Boolean aBoolean = enable.get("");
        System.out.println(aBoolean);
        if (aBoolean) {
            System.out.println("是的");
        } else {
            System.out.println("不是的");
        }
    }
}
