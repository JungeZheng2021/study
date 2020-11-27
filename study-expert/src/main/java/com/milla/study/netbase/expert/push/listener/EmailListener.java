package com.milla.study.netbase.expert.push.listener;

import com.milla.study.netbase.expert.push.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Package: com.milla.study.netbase.expert.push.listener
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/09 15:21
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/09 15:21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Component
public class EmailListener implements ApplicationListener<OrderEvent> {
    @Override
    public void onApplicationEvent(OrderEvent orderEvent) {
        log.info("邮箱邮件通知：{}", orderEvent);
    }
}
