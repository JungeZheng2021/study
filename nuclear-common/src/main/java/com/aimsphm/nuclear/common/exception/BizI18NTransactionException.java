package com.aimsphm.nuclear.common.exception;

import com.aimsphm.nuclear.common.response.RestResponseCode;

public class BizI18NTransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String[] placeholders = new String[0];

	public String[] getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(String[] placeholders) {
		this.placeholders = placeholders;
	}

	public BizI18NTransactionException() {
		super();
	}

	public BizI18NTransactionException(String msg) {
		super(msg);
	}

	public BizI18NTransactionException(RestResponseCode code, String... placeholders) {
		super(code.getLabel());
		this.code = code;
		this.placeholders = placeholders;
	}

	public RestResponseCode getCode() {
		return code;
	}

	private RestResponseCode code;
}
