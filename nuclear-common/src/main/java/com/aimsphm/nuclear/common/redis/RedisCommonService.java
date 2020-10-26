package com.aimsphm.nuclear.common.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.excelutil.ExcelUploadSupport;

@Service
public class RedisCommonService {
	@Autowired
	RedisClient redisClient;

	public void setUploadCache(Long batchUploadId, List<? extends ExcelUploadSupport> vos) {
		redisClient.setList(batchUploadId.toString(), vos);
	}

	public List<? extends ExcelUploadSupport> getUploadCache(Long batchUploadId) {
		List<? extends ExcelUploadSupport> list = redisClient.getList(batchUploadId.toString());
		return list;
	}

	public boolean clearCacheUpload(Long batchUploadId) {
		redisClient.remove(batchUploadId.toString());
		return true;
	}

}
