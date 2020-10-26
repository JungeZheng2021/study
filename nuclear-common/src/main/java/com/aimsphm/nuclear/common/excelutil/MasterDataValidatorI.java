package com.aimsphm.nuclear.common.excelutil;

import java.util.List;

import com.aimsphm.nuclear.common.excelutil.ExcelUploadSupport;


public interface MasterDataValidatorI<T extends ExcelUploadSupport> {
	
	public List<? extends ExcelUploadSupport> validateUpload(List<? extends ExcelUploadSupport> list);
	List<? extends ExcelUploadSupport> validateUploadDelete(List<? extends ExcelUploadSupport> list);
}
