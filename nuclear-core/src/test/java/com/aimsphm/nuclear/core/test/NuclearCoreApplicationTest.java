package com.aimsphm.nuclear.core.test;

import com.aimsphm.nuclear.common.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class NuclearCoreApplicationTest {

    @Autowired
    private RedisClient client;

    @Test
    public void testContent() throws Exception {
        boolean downSampleHourly = client.lock("downSampleHourly");
        log.info("获取锁结果：{}", downSampleHourly);
        if (!downSampleHourly) {
            Boolean downSampleHourly1 = client.delete("downSampleHourly");
            log.info("删除锁结果：{}", downSampleHourly1);
            if (downSampleHourly1) {
                boolean downSampleHourly2 = client.lock("downSampleHourly");
                log.info("重新获取获取锁结果：{}", downSampleHourly2);
            }
        }
        Boolean downSampleHourly1 = client.delete("downSampleHourly");
        log.info("删除锁结果：{}", downSampleHourly1);
    }
}
