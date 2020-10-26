package com.aimsphm.nuclear.common.pojo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;

@Data
public class QueryObject<T> {
	private Page<T> page;
	private T queryObject;

	
}
