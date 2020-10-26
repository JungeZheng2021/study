package com.aimsphm.nuclear.common.exception;

public class InvalidStartStopRecordException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidStartStopRecordException() {
		super();
	}

	public InvalidStartStopRecordException(String msg) {
		super(msg);
	}

	public InvalidStartStopRecordException(Throwable e) {
		super(e);
	}
}
