package com.milla.study.netbase.expert;

import com.milla.study.netbase.expert.push.event.OrderEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class StudyExpertApplicationTests {


    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        OrderEvent orderEvent = new OrderEvent(this);
        context.publishEvent(orderEvent);
    }

}
