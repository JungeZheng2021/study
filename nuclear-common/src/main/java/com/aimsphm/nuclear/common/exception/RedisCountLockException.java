package com.aimsphm.nuclear.common.exception;

public class RedisCountLockException extends Exception {
	private static final long serialVersionUID = 1L;

	public RedisCountLockException() {
		super();
	}
	
	public RedisCountLockException(String msg) {
		super(msg);
	}
	
	public RedisCountLockException(Throwable e) {
		super(e);
	}
}
