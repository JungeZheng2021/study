package com.aimsphm.nuclear.algorithm.core.model;

import java.io.Serializable;

import lombok.Data;

@Data
public  class InputParameter<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8386930413892962022L;
	private String algorithmType;
	private Long invokingTime;
	private Integer algorithmPeriod;
	private String algoFileName;
	private String folderName;
	private Long subSystemId;
	private T data;

}
