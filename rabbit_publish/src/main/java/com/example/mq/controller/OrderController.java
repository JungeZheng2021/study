package com.study.mq.controller;

import com.study.mq.service.IOrderService;
import com.study.mq.util.entity.DataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.example.mq.controller
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/05 09:50
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/05 09:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = {""}, produces = {"application/json"})
public class OrderController {
    @Autowired
    private IOrderService service;

    public OrderController() {
    }

    @GetMapping({"saving"})
    public int saveOrder() {
        return this.service.saveOrder();
    }


    @GetMapping({"start"})
    public boolean test(DataQuery query) {
        int number = (int) (5 + Math.random() * (10 + 1));

        for (int j = 0; j < number; ++j) {
            this.service.enable(query);
        }

        return true;
    }

    @GetMapping({"stop"})
    public boolean stop(String url) {
        this.service.disabled(url);
        return true;
    }
}
