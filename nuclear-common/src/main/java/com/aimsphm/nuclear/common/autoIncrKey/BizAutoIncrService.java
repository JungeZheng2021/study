package com.aimsphm.nuclear.common.autoIncrKey;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aimsphm.nuclear.common.mapper.BizAutoIncrMapper;

@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true",matchIfMissing= false)
public class BizAutoIncrService {
	private static final int INCREASE = 1;
	private static final int INIT_NUM = 0;
	@PostConstruct
	void initAutoIncrService() throws Exception {
//		init();
	}
	@Autowired
	BizAutoIncrMapper bizAutoIncrMapper;

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int nextSerial(String key) {
		if (key == null) {
			throw new NullPointerException();
		}

		while (true) {
			BizAutoIncr currernt = bizAutoIncrMapper.selectByBizKey(key);
			if (currernt == null) {
				throw new IllegalStateException("No record for biz key: " + key + "  in database.");
			}
			Integer nextVal = currernt.getValue() + INCREASE;

			int affectedRows = bizAutoIncrMapper.updateWithLagerValue(key, nextVal);

			if (affectedRows > 1) {
				throw new IllegalStateException("More than one record updated.");
			} else if (affectedRows == 1) {
				return nextVal;
			} else {
				continue;
			}
		}
	}

	public void removeAll() {
		bizAutoIncrMapper.deleteAll();
	}

	public int insert(List<BizAutoIncr> bizAutoIncrs) {
		if (bizAutoIncrs == null || bizAutoIncrs.isEmpty()) {
			return 0;
		}

		return bizAutoIncrMapper.insertBatch(bizAutoIncrs);
	}

	public List<BizAutoIncr> getAll() {
		return bizAutoIncrMapper.selectAll();
	}

	@Transactional
	public void init() {
		Set<String> existedKeys = bizAutoIncrMapper.selectAll().stream().map(i -> i.getBizKey())
				.collect(Collectors.toSet());

		List<BizAutoIncr> entries = Arrays.asList(BizAutoIncrKey.values()).stream()
				.filter(ik -> !existedKeys.contains(ik.getValue())).map(ik -> {
					BizAutoIncr i = new BizAutoIncr();
					i.setBizKey(ik.getValue());
					i.setValue(INIT_NUM);
					return i;
				}).collect(Collectors.toList());
		if (!entries.isEmpty()) {
			bizAutoIncrMapper.insertBatch(entries);
		}
	}
}
