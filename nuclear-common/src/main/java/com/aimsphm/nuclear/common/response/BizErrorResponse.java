package com.aimsphm.nuclear.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BizErrorResponse<T> extends ReturnResponse<T> {

	public BizErrorResponse(RestResponseCode resCode) {
		if (resCode == null) {
			return;
		}

		this.resCode = resCode.getCode();
		this.errMsg = resCode.getLabel();
	}

	public BizErrorResponse(String errorMessage) {
		resCode = RestResponseCode.ERROR.getCode();
		errMsg = errorMessage;
	}

	
	private String errMsg;
}
