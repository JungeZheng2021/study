package com.aimsphm.nuclear.executor.bo;

import lombok.Data;

@Data
public class TestReqParam {
    private long startTimestamp;
    private long endTimestamp;
    private int loop;
    private String freq;
    private int partitions;
}
