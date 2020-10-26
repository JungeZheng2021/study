package com.aimsphm.nuclear.common.excelutil;

import java.lang.reflect.Type;

/**
 * Excel用户标题
 * 
 * @author
 * 
 */
public class ExcelHeader {
	private String methodName; // 字段的方法名称(字符串形式,保存字段的getXXX方法)
	private String title; // 字段在Excel中对应的标题
	private Type type; // 字段的类型
	private Boolean checkNull;
	private String numrangeCheck;
	private String contentCheck;
	private String timeunit;

	public String getTimeunit() {
		return timeunit;
	}

	public void setTimeunit(String timeunit) {
		this.timeunit = timeunit;
	}

	public String getContentCheck() {
		return contentCheck;
	}

	public void setContentCheck(String contentCheck) {
		this.contentCheck = contentCheck;
	}

	public String getNumrangeCheck() {
		return numrangeCheck;
	}

	public void setNumrangeCheck(String numrangeCheck) {
		this.numrangeCheck = numrangeCheck;
	}

	public Boolean getCheckNull() {
		return checkNull;
	}

	public void setCheckNull(Boolean checkNull) {
		this.checkNull = checkNull;
	}

	public ExcelHeader() {
	}

	public ExcelHeader(String methodName, String title, Type type, Boolean checkNull, String numrangeCheck,
			String contentCheck, String timeunit) {
		this.methodName = methodName;
		this.title = title;
		this.type = type;
		this.checkNull = checkNull;
		this.numrangeCheck = numrangeCheck;
		this.contentCheck = contentCheck;
		this.timeunit = timeunit;

	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//    public int getOrder() {  
//        return order;  
//    }  
//    public void setOrder(int order) {  
//        this.order = order;  
//    }  

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 根据order进行排序
	 */
//    @Override  
//    public int compareTo(ExcelHeader o) {  
//        return this.order > o.order ? -1 : (this.order < o.order ? 1 : 0) ;  
//    }  
}