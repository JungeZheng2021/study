package com.aimsphm.nuclear.down.sample;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.down.sample.entity.bo.ThreePointBO;
import com.aimsphm.nuclear.down.sample.service.impl.MonthlyDownSampleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@Slf4j
@SpringBootTest
//@RunWith(SpringRunner.class)
public class NuclearDownSampleApplicationTests {

    @Test
    public void contextLoads() {
//        Long start = DateUtils.getStartOfPreviousDay(System.currentTimeMillis());
//        Long end = DateUtils.getEndOfPreviousDay(System.currentTimeMillis());
//        System.out.println(new Date(start));
//        System.out.println(new Date(end));
//        System.out.println(DateUtils.isStartOfDay(1630425599000L));
        MonthlyDownSampleServiceImpl service = new MonthlyDownSampleServiceImpl();
        Map<TimeRangeQueryBO, ThreePointBO> mp = service.initKeys(1627747200000L, 1630425599000L, 838);
        mp.entrySet().stream().sorted(Comparator.comparing(a -> a.getKey().getEnd())).forEach(x -> {
            log.info("{}", x);
        });
//        test(1627747200000L, 1630425599000L, 300);
    }

    private void test(Long start, Long end, Integer targetNumber) {
        List<TimeRangeQueryBO> objects = new ArrayList<>(16);
        int gap = (int) ((end - start) / 1000 / targetNumber);
        long begin = start;
        for (int i = 0; i < targetNumber; i++) {
            if (i == 247) {
                System.out.println(i);
            }
            TimeRangeQueryBO bo = new TimeRangeQueryBO(begin + i * gap * 1000L, begin + (i + 1) * gap * 1000L);
            log.info("{}   \t,-> {}", i, bo);
            objects.add(bo);
        }
        long last = begin + gap * targetNumber * 1000L;
        if (last != end) {
            objects.add(new TimeRangeQueryBO(last, end));
        }
        objects.stream().forEach(x -> {
            log.info("{}", x);
//            log.info("{}~{}::{}~{}", x.getStart(), x.getEnd(), DateUtils.format(x.getStart()), DateUtils.format(x.getEnd()));
        });
    }
}
