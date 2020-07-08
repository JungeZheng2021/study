package com.milla.study.netbase.expert.concurrent.forkjoin;

import com.google.common.collect.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.forkjoin
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/18 19:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/18 19:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class CountDownTest {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new CountDownTest().test();
    }

    private void test() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        List<String> urlList = initResources("https://blog.csdn.net/hu10131013");
        CountDownLatch count = new CountDownLatch(urlList.size());
        List<String> data = Lists.newArrayList();
        long begin = System.currentTimeMillis();
        List<FutureTask<String>> tasks = Lists.newArrayList();
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
//                    count.countDown();
                    long start = System.currentTimeMillis();
                    Thread.sleep(new Random().nextInt(3000));
                    ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
                    long end = System.currentTimeMillis();
                    String reasonPhrase = forEntity.getStatusCode().getReasonPhrase();
                    System.out.println(url + " 共计耗时： " + (end - start));
                    return reasonPhrase;
                }
            };
            FutureTask<String> task = new FutureTask<>(callable);
            new Thread(task).start();
            tasks.add(task);
        }
        System.out.println("没有全部执行完");
        System.out.println(data);
//        count.await();

        for (int i = 0; i < tasks.size(); i++) {
            FutureTask<String> task = tasks.get(i);
            data.add(task.get());
        }
        long dataLine = System.currentTimeMillis();
        System.out.println("都执行结束之后在打印:" + (dataLine - begin));
        System.out.println(data);


    }

    private List<String> initResources(String url) {
        List<String> resources = Lists.newArrayList();
        String splitText = "article-item-box csdn-tracking-statistics";
        String preText = "\" data-articleid=\"";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        String[] split = body.split(splitText);
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (StringUtils.hasText(s) && s.startsWith(preText)) {
                String number = "</span>\n" +
                        "                <span class=\"read-num\">";
                String[] numbers = s.split(number);
                String startText = "href=\"";
                String endText = "\" target=\"_blank\"";
                int start = s.indexOf(startText);
                int end = s.indexOf(endText);
                String index = s.substring(start + startText.length(), end);
                resources.add(index);
                String id = index.substring(index.lastIndexOf("/") + 1);
                for (int j = 0; j < numbers.length; j++) {
                    String numberText = numbers[j];
                    if (numberText != null && numberText.startsWith("<img") && !numberText.contains("div")) {
//                        init.put(id, Integer.parseInt(numberText.substring(numberText.lastIndexOf(">") + 1)));
                    }
                }
            }
        }
        return resources;
    }
}
