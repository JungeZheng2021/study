package com.aimsphm.nuclear.history.controller;

import com.aimsphm.nuclear.common.entity.bo.HColumnQueryBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseColumnItemDTO;
import com.aimsphm.nuclear.common.entity.dto.HBaseColumnItemsDTO;
import com.aimsphm.nuclear.common.entity.dto.HBaseFamilyDTO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTableDTO;
import com.aimsphm.nuclear.history.service.HBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.hbase.controller
 * @Description: <HBase数据库操作类>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 15:20
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 15:20
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "hbase", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(tags = "HBase服务操作接口")
public class HbaseController {

    @Autowired
    private HBaseService service;

    @PostMapping(value = "table")
    @ApiOperation(value = "新建一个表格", notes = "建表时至少有一个列族，默认使用SNAPPY算法进行压缩")
    public void saveTable(@RequestBody HBaseTableDTO table) {
        service.saveTable(table);
    }

    @PutMapping(value = "table/family")
    @ApiOperation(value = "增加一个列族")
    public void saveFamily2Table(@RequestBody HBaseTableDTO table) {
        service.saveFamily2Table(table);
    }

    @DeleteMapping(value = "table/{tableName}")
    @ApiOperation(value = "删除表格")
    public void removeTable(@PathVariable String tableName) {
        service.removeTable(tableName);
    }

    @DeleteMapping(value = "table/{tableName}/{family}")
    @ApiOperation(value = "删除指定的列族", notes = "当列族是唯一列族时删除失败")
    public void removeFamilyFromTable(@PathVariable String tableName, @PathVariable String family) {
        service.removeFamilyFromTable(tableName, family);
    }

    @GetMapping(value = "table/{tableName}")
    @ApiOperation(value = "获取表格的所有数据量", notes = "需要Hbase服务端配置插件[目前不能测试]")
    @Deprecated
    public Long getTableDataCount(@PathVariable String tableName) {
        return service.getTableDataCount(tableName);
    }

    @GetMapping(value = "table/{tableName}/desc")
    @ApiOperation(value = "获取表格的建表语句")
    public String getTableDescription(@PathVariable String tableName) {
        return service.getTableDescription(tableName);
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     */

    @PostMapping(value = "column")
    @ApiOperation(value = "保存一条数据")
    public void saveItemData2Table(@RequestBody HBaseColumnItemDTO itemDTO) {
        service.saveItemData2Table(itemDTO);
    }

    @PostMapping(value = "column/hour")
    @ApiOperation(value = "保存一条数据", notes = "列名会根据列的时间戳自动生成0~3599[需要校验rowKey中的时间和生成数据时间戳的关系]")
    public void saveItemData2TableByHour(@RequestBody HBaseColumnItemDTO itemDTO) {
        service.saveItemData2TableByHour(itemDTO);
    }

    @PostMapping(value = "column/batch")
    @ApiOperation(value = "批量插入数据")
    public void saveItemsData2Table(@RequestBody HBaseColumnItemsDTO itemsDTO) {
        service.saveItemsData2Table(itemsDTO);
    }

    @DeleteMapping(value = "rows/{tableName}/{rowKey}")
    @ApiOperation(value = "根据rowKey删除数据")
    public void removeDataFromTable(@RequestBody HBaseFamilyDTO removeDTO, @PathVariable String tableName, @PathVariable String rowKey) {
        service.removeDataFromTable(tableName, rowKey, removeDTO);
    }


    @GetMapping(value = "data/{tableName}/{rowKey}")
    @ApiOperation(value = "根据rowKey获取数据", notes = "按照不同的列族和列进行返回")
    public List<Map<String, Object>> getDataByRowKey(@PathVariable String tableName, @PathVariable String rowKey, HBaseFamilyDTO familyDTO) {
        return service.getDataByRowKey(tableName, rowKey, familyDTO);
    }

    @GetMapping(value = "data")
    @ApiOperation(value = "获取一定时间区间内的数据", notes = "按照不同的列族和列进行返回")
    public List<Map<String, Object>> listData(HColumnQueryBO queryBo) {
        return service.listData(queryBo);
    }
}
