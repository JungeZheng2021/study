package com.study.mq;

import lombok.Data;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SpringBootTest
class RabbitTestApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        Demo demo = new Demo("12", "acc", "十二");
        Demo demo1 = new Demo("13", "acc", "十三");
        Demo demo2 = new Demo("14", "acc", "十四");
        Demo demo3 = new Demo("21", "vec", "二十一");
        Demo demo4 = new Demo("22", "vec", "二十二");
        Demo demo5 = new Demo("31", "temp", "三十一");
        List<Demo> demos = Lists.newArrayList(demo, demo1, demo2, demo3, demo4, demo5);
        Map<String, List<Demo>> collect = demos.stream().collect(Collectors.groupingBy(Demo::getType, () -> new TreeMap().descendingMap(), Collectors.toList()));
        System.out.println(collect);
    }

    @Data
    static class Demo {

        private String tag;

        private String type;

        private String name;

        public Demo(String tag, String type, String name) {
            this.tag = tag;
            this.type = type;
            this.name = name;
        }
    }
}
