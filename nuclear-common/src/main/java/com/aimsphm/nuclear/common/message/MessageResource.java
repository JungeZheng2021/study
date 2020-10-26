package com.aimsphm.nuclear.common.message;

import java.util.Locale;

public interface MessageResource {

    abstract public String getMessage(String msgId);
    
    abstract public String getMessage(String msgId, String Parameter);
    
    abstract public String getMessage(String msgId, String[] bindList);
    
    abstract void applyLocale(Locale locale);
}
