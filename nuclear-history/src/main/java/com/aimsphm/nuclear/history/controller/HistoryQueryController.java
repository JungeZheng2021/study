package com.aimsphm.nuclear.history.controller;

/**
 * @Package: com.aimsphm.nuclear.history.controller
 * @Description: <历史数据查询-相关接口>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 11:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 11:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = "历史数据查询-相关接口")
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryQueryController {
    @Autowired
    HBaseUtil hBaseUtil;

    private HistoryQueryService service;

    public HistoryQueryController(HistoryQueryService service) {
        this.service = service;
    }

    @GetMapping("single")
    @ApiOperation(value = "查询一个测点的历史数据", notes = "PI 测点feature不需要传值,自装测点需要传特征值")
    public HistoryDataVO listHistoryWithSingleTag(HistoryQuerySingleBO singleBO) {
        long stat = System.currentTimeMillis();
        HistoryDataVO data = service.listHistoryDataWithSingleTagByScan(singleBO);
        System.out.println("共计耗时：" + (System.currentTimeMillis() - stat));
        return data;
    }

    @GetMapping("multiple")
    @ApiOperation(value = "查询多个测点的历史数据(多个测点的同一个特征)", notes = "PI 测点feature不需要传值,自装测点需要传特征值")
    public String listHistoryWithTagList(HistoryQueryMultiBO queryMultiBO) throws IOException {
        String npc_phm_data = hBaseUtil.tableDescription("npc_phm_data");
        return npc_phm_data;
    }

}
