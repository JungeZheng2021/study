package com.milla.study.netbase.expert.concurrent.forkjoin;

import com.google.common.collect.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.*;

import static org.apache.xmlbeans.impl.store.Public2.test;

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
public class ForkJoinTest {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ForkJoinTest().test();
    }

    private void test() throws ExecutionException, InterruptedException {

        List<String> urlList = initResources("https://blog.csdn.net/hu10131013");
        ForkJoinPool pool = new ForkJoinPool
                (Runtime.getRuntime().availableProcessors(),
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        null, true);

        ForkJoinTask<?> submit = pool.submit(new Work(restTemplate, urlList, 0, urlList.size() - 1));
        Object o = submit.get();
        System.out.println(o);

    }

    class Work extends RecursiveTask<String> {
        private RestTemplate restTemplate;
        List<String> urlList;
        private int start;
        private int end;

        public Work(RestTemplate restTemplate, List<String> urlList, int start, int end) {
            this.restTemplate = restTemplate;
            this.urlList = urlList;
            this.start = start;
            this.end = end;
        }

        @Override
        protected String compute() {
            int count = end - start;
            System.out.println("count： " + count);
            if (count == 0) {
                ResponseEntity<String> forEntity = restTemplate.getForEntity(urlList.get(start), String.class);
                String reasonPhrase = forEntity.getStatusCode().getReasonPhrase();
                System.out.println(urlList.get(start) + " 请求结果： " + reasonPhrase);
                return reasonPhrase;
            } else {
                int middle = (start + end) / 2;
                System.out.println("拆分次数");
                Work work1 = new Work(restTemplate, urlList, start, middle);
                work1.fork();
                Work work2 = new Work(restTemplate, urlList, middle + 1, end);
                work2.fork();

                StringBuilder sb = new StringBuilder();
                sb.append(work1.join()).append("--").append(work2.join());
                return sb.toString();
            }
        }
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
