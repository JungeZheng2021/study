package com.milla.study.netbase.expert.concurrent;


import com.google.common.collect.Lists;
import com.milla.study.netbase.expert.concurrent.juc.ConcurrentHashMap1_7;
import com.milla.study.netbase.expert.concurrent.map.ConcurrentHashMap7;
import com.milla.study.netbase.expert.concurrent.map.HashMap7;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/29 13:50
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/29 13:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class AbaDemo {
    // 模拟充值
    // 有3个线程在给用户充值，当用户余额少于20时，就给用户充值20元。
    // 有100个线程在消费，每次消费10元。用户初始有9元
    static AtomicInteger money = new AtomicInteger(19);

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);

    public static void main(String[] args) {
        test();
        testStamped();

    }

    private static void testStamped() {
    }

    private static void test() {
        new Thread(() -> {
            log.info("操作结果：{},值为：{}", atomicReference.compareAndSet(100, 101), atomicReference.get());
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("其中我做了很多其他的事情");
        }, "t1").start();

        new Thread(() -> {
            log.info("操作结果：{},值为：{}", atomicReference.compareAndSet(101, 100), atomicReference.get());
        }, "t2").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("操作结果：{},值为：{}", atomicReference.compareAndSet(100, 2020), atomicReference.get());
        }, "t3").start();
    }

//
//    HashMap<String, String> map = new HashMap<>();
//
//    public void hashMapTest() {
//        for (int i = 0; i < 2000; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for (int j = 0; j < 500; j++) {
//                        map.put(Thread.currentThread().getName() + "-" + j, j + "");
//                    }
//                }
//            }).start();
//        }
//        try {
//            Thread.sleep(2000);
////            map.forEach((x,y) -> System.out.println(x));
//            System.out.println(map.size());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private static void test7() {

        //        System.out.println(System.currentTimeMillis());
        RestTemplate template = new RestTemplate();
        List<String> objects = Lists.newArrayList("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1", "Mozilla/5.0 (X11; CrOS i686 2268.111.0) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.57 Safari/536.11",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1092.0 Safari/536.6",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.6 (KHTML, like Gecko) Chrome/20.0.1090.0 Safari/536.6",
                "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/19.77.34.5 Safari/537.1",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.9 Safari/536.5",
                "Mozilla/5.0 (Windows NT 6.0) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.36 Safari/536.5",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_0) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1063.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1062.0 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/536.3",
                "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.0 Safari/536.3",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24",
                "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/535.24 (KHTML, like Gecko) Chrome/19.0.1055.1 Safari/535.24");
        String pre = "https://blog.csdn.net/hu10131013/article/details/";
        List<String> urlList = Lists.newArrayList(
                "106570661",
                "105598920",
                "106528023",
                "106503023",
                "106495571",
                "106478434",
                "106430788",
                "106124960",
                "106003019",
                "105822108",
                "105665232",
                "105654506",
                "105652522",
                "105438710",
                "104995465",
                "104799532",
                "105663468",
                "105600135",
                "103794603",
                "103754999",
                "102901137",
                "102899281",
                "52041893"
        );

        while (true) {
            try {
                try {
                    Random random = new Random();
                    int i = random.nextInt(objects.size());
                    int urlIndex = random.nextInt(urlList.size());
                    HttpHeaders requestHeaders = new HttpHeaders();
                    if (i < objects.size()) {
                        requestHeaders.add("User-Agent", objects.get(i));
                    }
                    String url = pre + urlList.get(urlIndex == urlList.size() ? urlIndex - 1 : urlIndex);
                    HttpEntity<Object> entity = new HttpEntity<>(null, requestHeaders);
                    ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);
                    System.out.println("访问成功！！");
                    Thread.sleep(2000L);
                } finally {

                }
            } catch (RestClientException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }

    }

}
