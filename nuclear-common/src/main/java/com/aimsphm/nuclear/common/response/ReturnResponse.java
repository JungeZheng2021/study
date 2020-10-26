package com.aimsphm.nuclear.common.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnResponse<T> {

	protected String resCode = RestResponseCode.OK.getCode();
	protected T resData;
}
