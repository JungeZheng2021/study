package com.milla.study.netbase.expert.quartz.listener;

import com.milla.study.netbase.expert.quartz.service.QuartzJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Package: com.milla.study.netbase.expert.quartz.listener
 * @Description: <启动的时候执行>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Order(value = 1)
public class QuartzJobInitListener implements CommandLineRunner {

    @Autowired
    //所有实现初始化的接口都注入进来，然后遍历执行
    Map<String, QuartzJobService> scheduleJobServiceMap;

    @Override
    public void run(String... arg0) throws Exception {
        if (scheduleJobServiceMap == null || scheduleJobServiceMap.isEmpty()) {
            return;
        }
        Set<Map.Entry<String, QuartzJobService>> entries = scheduleJobServiceMap.entrySet();
        Iterator<Map.Entry<String, QuartzJobService>> it = entries.iterator();
        for (; it.hasNext(); ) {
            Map.Entry<String, QuartzJobService> next = it.next();
            //为防止报错，先给一个默认实现
            if ("default".equals(next.getKey())) {
                continue;
            }
            QuartzJobService service = next.getValue();
            service.initSchedule();
        }
    }

}
