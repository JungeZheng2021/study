package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.core.service.DeviceStartStopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("deviceStartStop")
@Api(tags = "启停记录维护接口")
public class DeviceStartAndStopController {

    @Autowired
    DeviceStartStopService deviceStartStopService;

    @GetMapping("getRecordsByDeviceIdAndType")
    @ApiOperation(value = "根据设备分组编号和种类搜寻相应的启停记录")
    public Object getRecordsByDeviceIdAndDeviceType(@RequestParam("deviceId") Long deviceId,@RequestParam("deviceType") Integer deviceType){
        return ResponseUtils.success(deviceStartStopService.getRecordsByDeviceIdAndType(deviceId,deviceType));
    }

    @GetMapping("getRecordsByeId")
    @ApiOperation(value = "根据数据库记录id查找启停记录")
    public Object getRecordsById(@RequestParam("id") Long id){
        return ResponseUtils.success(deviceStartStopService.getRecordById(id));
    }

    @PostMapping("create")
    @ApiOperation(value = "新建一条启停记录",notes = "Post记录中id可以为空")
    public Object create(@RequestBody TxDeviceStopStartRecord record) {
        try {
            Boolean result = deviceStartStopService.createRecord(record);
            return ResponseUtils.success(result);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseUtils.error("创建记录失败");
        }

    }

    @PostMapping("update")
    @ApiOperation(value = "更新一条启停记录")
    public Object update(@RequestBody TxDeviceStopStartRecord record) {
        try {
            Boolean result = deviceStartStopService.updateRecord(record);
            return ResponseUtils.success(result);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseUtils.error("跟新记录失败，请查看前后记录的时间");
        }

    }

    @DeleteMapping("delete/{id}")
    @ApiOperation(value = "根据数据库id删除一条启停记录")
    public Object delete(@PathVariable("id") Long id){
        try {

            Boolean result = deviceStartStopService.deleteRecord(id);
            return ResponseUtils.success(result);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseUtils.error("删除记录失败");
        }
    }
}
