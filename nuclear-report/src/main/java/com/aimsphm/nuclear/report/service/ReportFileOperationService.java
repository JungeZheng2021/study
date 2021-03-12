package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.entity.vo.SensorTrendVO;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.report.service
 * @Description: <图表操基类-图表文件>
 * @Author: MILLA
 * @CreateDate: 2020/4/28 17:49
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/28 17:49
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface ReportFileOperationService {

    Map<String, File> getPumpScatterDataFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;

    File getPumpBarFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;

    List<Map<String, File>> getPumpPieFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;
}
