package com.aimsphm.nuclear.common.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aimsphm.nuclear.common.constant.CommonConstant;

public class PropertyMessage implements MessageResource {
	private static ResourceBundle resource;
	private static ResourceBundle resource_CN;
	private static ResourceBundle resource_EN;

	PropertyMessage() {
		resource = ResourceBundle.getBundle("com.aimsphm.nuclear.common.message.exceptions");
		resource_CN = ResourceBundle.getBundle("com.aimsphm.nuclear.common.message.exceptions", Locale.CHINA);
		resource_EN = ResourceBundle.getBundle("com.aimsphm.nuclear.common.message.exceptions", Locale.US);
	}

	PropertyMessage(Locale locale) {
		resource = ResourceBundle.getBundle("com.aimsphm.nuclear.common.message.exceptions", locale);
	}

	@Override
	public String getMessage(String msgId) {
		String locale = getLocale();
		ResourceBundle rb = getResourceBundle(locale);

		return rb.getString(msgId);

	}

	private ResourceBundle getResourceBundle(String locale) {
		ResourceBundle resourceBundle = getRelateResourceBundle(locale);
		return resourceBundle;
	}

	private ResourceBundle getRelateResourceBundle(String locale) {
		if (locale == null || locale.trim().equals("")) {
			return resource;
		} else if (locale != null && locale.trim().equals(CommonConstant.I18N_EN)) {
			return resource_EN;
		} else if (locale != null && locale.trim().equals(CommonConstant.I18N_CN)) {
			return resource_CN;
		} else {
			return resource;
		}
	}

	@Override
	public String getMessage(String msgId, String Parameter) {
		String locale = getLocale();
		return MessageFormat.format(getRelateResourceBundle(locale).getString(msgId), Parameter);
	}

	private String getLocale() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String locale = "";
		if (attributes != null) {
			locale = (String) attributes.getAttribute(CommonConstant.I18N_LOCALE, RequestAttributes.SCOPE_REQUEST);
		}
		return locale;
	}

	@Override
	public String getMessage(String msgId, String[] bindList) {
		String locale = getLocale();
		return MessageFormat.format(getRelateResourceBundle(locale).getString(msgId), (Object[]) bindList);
	}

	@Override
	public void applyLocale(Locale locale) {
		resource = ResourceBundle.getBundle("com.aimsphm.nuclear.common.message.exceptions", locale);
	}

}