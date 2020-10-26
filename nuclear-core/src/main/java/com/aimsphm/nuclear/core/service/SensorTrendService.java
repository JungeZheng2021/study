package com.aimsphm.nuclear.core.service;

import java.util.List;
import java.util.Map;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.vo.SensorTrendVO;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <测点趋势相关>
 * @Author: lu.yi
 * @CreateDate: 2020/4/3 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface SensorTrendService {

	List<Map<String,Object>> getTrendHotSpotBySubSystemId(Long deviceId);

	List<Map<String,Object>> getTrendHotSpot(Long deviceId);

	Map<String, SensorTrendVO> getSubSystemTrendHotSpotDetails(Long subSystemId);

	Map<String, SensorTrendVO> getTrendHotSpotDetails(List<String> tagIds);

	Map<String, SensorTrendVO> getTrendHotSpotDetails(Long deviceId);

	
}
