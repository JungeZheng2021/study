package com.aimsphm.nuclear.common.quartz.listener;

import com.aimsphm.nuclear.common.quartz.service.QuartzJobService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 功能描述:启动的时候执行
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 19:38
 */
@Component
@Order(value = 1)
public class QuartzJobInitListener implements CommandLineRunner {

    @Resource
    Map<String, QuartzJobService> scheduleJobServiceMap;

    @Override
    public void run(String... arg0) throws Exception {
        if (scheduleJobServiceMap == null || scheduleJobServiceMap.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, QuartzJobService>> entries = scheduleJobServiceMap.entrySet();
        Iterator<Map.Entry<String, QuartzJobService>> it = entries.iterator();
        while (it.hasNext()) {
            Map.Entry<String, QuartzJobService> next = it.next();
            if ("default".equals(next.getKey())) {
                continue;
            }
            QuartzJobService service = next.getValue();
            service.initSchedule();
        }
    }

}
