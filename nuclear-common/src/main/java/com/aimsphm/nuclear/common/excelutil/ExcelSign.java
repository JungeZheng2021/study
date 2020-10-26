package com.aimsphm.nuclear.common.excelutil;

import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)  
public @interface ExcelSign {  
    String title() ;  //记录每个字段在Excel中的标题  
    boolean checkNull() default false;
    String contentCheck() default "";
    String numrangeCheck() default "";
    String timeunit() default "";
}  
