package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.TxReport;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.entity.vo.AlarmEventVO;
import com.aimsphm.nuclear.common.entity.vo.PumpHealthVO;
import com.aimsphm.nuclear.common.entity.vo.WarmingPointsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 报告表
 *
 * @author lu.yi
 * @since 2020-04-30
 */
@Repository
public interface TxReportMapper extends BaseMapper<TxReport> {

    List<AlarmEventVO> selectAlarmRecordBySubSystemId(ReportQueryBO queryBO);

    List<PumpHealthVO> selectPumpHealthBySubSystemId(ReportQueryBO query);

    List<WarmingPointsVO> selectWarmingPointsByDateDistribution4Report(@Param("subSystemId") Long subSystemId, @Param("endDate") Date date, @Param("days") long days);
}