package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能描述:图表操基类-图表文件
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/28 17:49
 */
public interface ReportFileOperationService {

    Map<String, File> getPumpScatterDataFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;

    File getPumpBarFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;

    List<Map<String, File>> getPumpPieFile(ReportQueryBO query, Long sleepTime) throws InterruptedException;
}
