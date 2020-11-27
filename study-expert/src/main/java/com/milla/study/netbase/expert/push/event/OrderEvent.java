package com.milla.study.netbase.expert.push.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Package: com.milla.study.netbase.expert.push.event
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/09 14:40
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/09 14:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class OrderEvent extends ApplicationEvent {
    public OrderEvent(Object source) {
        super(source);
    }
}
