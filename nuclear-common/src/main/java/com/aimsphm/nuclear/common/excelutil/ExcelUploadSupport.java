package com.aimsphm.nuclear.common.excelutil;

import java.util.List;


public interface ExcelUploadSupport {

	public int getUploadStatus();

	public void setUploadStatus(int uploadStatus);

	public void setBatchUploadID(long batchUploadID);

	public List<String> getErrors();

	public void addError(String error);

	public void setErrors(List<String> errors);

	String getCombinedKey();

	public void setId(Long id);

	public Long getId();
}
