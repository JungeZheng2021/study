package com.example.mq.util.entity;

import lombok.Data;

/**
 * @Package: com.example.mq.util.entity
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/10 16:48
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/10 16:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class DataQuery {
    private String url;
    private Integer pages;
    private Long sleepTime;
}