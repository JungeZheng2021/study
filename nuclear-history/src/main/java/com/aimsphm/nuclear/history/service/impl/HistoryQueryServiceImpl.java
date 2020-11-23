package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.regionserver.NoSuchColumnFamilyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;

/**
 * @Package: com.aimsphm.nuclear.history.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class HistoryQueryServiceImpl implements HistoryQueryService {

    private HBaseUtil hBase;

    public HistoryQueryServiceImpl(HBaseUtil hBase) {
        this.hBase = hBase;
    }

    @Override
    public List<HBaseTimeSeriesDataDTO> listHistoryWithSingleTagByScan(HistoryQuerySingleBO single) {
        if (Objects.isNull(single) || Objects.isNull(single.getTagId())) {
            return null;
        }
        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        if (StringUtils.isNotBlank(single.getFeature())) {
            family = single.getFeature();
        }
        try {
            return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, single.getTagId(), single.getStart(), single.getEnd(), family);
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }

    @Override
    public List<HBaseTimeSeriesDataDTO> listHistoryWithSingleTagByRowKeyList(HistoryQuerySingleBO singleBO) {
        return null;
    }
}
