package com.aimsphm.nuclear.down.sample.job;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.down.sample.service.DownSampleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/02 08:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DownSampleJobTest {
    @Resource
    private DownSampleService downSampleService;

    @Test
    public void test() {
        TimeRangeQueryBO bo = new TimeRangeQueryBO(System.currentTimeMillis() - 60 * 24 * 3600_000, System.currentTimeMillis());
        downSampleService.allData(bo);
    }

}