package com.aimsphm.nuclear.core.feign;


import com.aimsphm.nuclear.common.entity.dto.Cell;
import com.aimsphm.nuclear.common.entity.dto.TrendFeatureCellDTO;
import com.aimsphm.nuclear.common.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Component
@FeignClient(value = "algorithm-core")
public interface AlgorithmServiceFeignClient {
    @ApiOperation(value = "拿到一组tag的滑动平均值", notes = "拿到一组tag的滑动平均值")
    @GetMapping("mdAlgorithm/getTrendFeatureByTags")
    public ResponseData<Map<String, List<TrendFeatureCellDTO>>> getTrendFeatureByTags(@RequestParam(required = false, value = "tags") List<String> tags, @RequestParam(required = false, value = "start") Long start, @RequestParam(required = false, value = "end") Long end);

    @GetMapping("mdAlgorithm/getVSDataByVSId")
    public ResponseData<List<Cell>> getVSDataByVSId(@RequestParam Long vsensorId, @RequestParam Long start, @RequestParam Long end, @RequestParam Integer step);

    @GetMapping("mdAlgorithm/getTagData")
    public ResponseData<List<Cell>> getTagData(@RequestParam String tagId, @RequestParam Long start, @RequestParam Long end, @RequestParam Integer step);
}
