package com.aimsphm.nuclear.common.message;

import java.util.MissingResourceException;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.response.RestResponseCode;


public class I18NHelper {
	public static String getI18NErrorMsg(RestResponseCode errorCode) {
    	if (errorCode == null) {
    	    return null;
    	}
    	String key = CommonConstant.I18N_PREFIX+ errorCode.getCode();
    	String i18nMsg = errorCode.getLabel();
    	try{
			 i18nMsg =	MessageResourceFactory.getMessageResource().getMessage(key);
		 }catch(MissingResourceException e){
			 e.printStackTrace();
		 }
    	return i18nMsg;

        }
	  public static String getI18NErrorMsg(RestResponseCode errorCode,String... placeholder) {
	    	if (errorCode == null) {
	    	    return null;
	    	}
	    	String key = CommonConstant.I18N_PREFIX+ errorCode.getCode();
	    	String i18nMsg = errorCode.getLabel();
	    	try{
				 i18nMsg =	MessageResourceFactory.getMessageResource().getMessage(key, placeholder);
			 }catch(MissingResourceException e){
				 e.printStackTrace();
			 }
	    	return i18nMsg;

	        }
}
