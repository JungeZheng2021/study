package com.aimsphm.nuclear.algorithm.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Package: com.aimsphm.nuclear.algorithm.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/18 18:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/18 18:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {

    @GetMapping("demo")
    @ApiOperation(value = "获取某一实体")
    public void getDeviceStateMonitorInfo1(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xlsx");
        List<List<Object>> list = new ArrayList<>();
        List<Object> item = new ArrayList<Object>() {{
            add(System.currentTimeMillis());
            add(new Random().nextDouble() * 1000);
        }};
        List<Object> item1 = new ArrayList<Object>() {{
            add(System.currentTimeMillis());
            add(new Random().nextDouble() * 1000);
        }};
        List<Object> item2 = new ArrayList<Object>() {{
            add(System.currentTimeMillis());
            add(new Random().nextDouble() * 1000);
        }};
        List<Object> item3 = new ArrayList<Object>() {{
            add(System.currentTimeMillis());
            add(new Random().nextDouble() * 1000);
        }};
        List<Object> item4 = new ArrayList<Object>() {{
            add(System.currentTimeMillis());
            add(new Random().nextDouble() * 1000);
        }};
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        ExcelWriter writer = EasyExcel.write(response.getOutputStream()).build();
        List<String> timestamp = Lists.newArrayList("timestamp");
        List<String> value = Lists.newArrayList("value");
        List<List<String>> headers = new ArrayList<>();
        headers.add(timestamp);
        headers.add(value);
        WriteSheet writeSheet = EasyExcel.writerSheet(1, "测试").head(headers).build();
        writer.write(list, writeSheet);
        writer.finish();
    }
}
