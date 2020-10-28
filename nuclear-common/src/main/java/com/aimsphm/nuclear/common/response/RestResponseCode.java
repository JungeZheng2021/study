
package com.aimsphm.nuclear.common.response;

public enum RestResponseCode {
    OK("00000", "OK"),
    ERROR("00001", "ERROR"),
    EXCEL_ERROR_01("01001", "{0} Column Error:Date format error {1}"),
    EXCEL_ERROR_02("01002", "Wrong Integer value for {0}: {1}"),
    EXCEL_ERROR_03("01003", "{0} Column Error:Can't assign null"),
    EXCEL_ERROR_04("01004", "{0} Column Error:Content mismatched {1}"),
    EXCEL_ERROR_05("01005", "{0} Column Number Range exceed:{1}"),
    DATE_FORMAT_ERROR("01001", "{0} Column Error:Date format error {1}"),
    STSTEM_EXCEPTION("01012", "System error, please contact the administrator"),
    HYSTRIX_EXCEPTION("01013", "系统忙, 请稍后再试"),
    FORCAST_EXCEPTION("01014", "选择的时间段的数据为空，无法预测"),
    RECOG_EXCEPTION("01015", "选择的时间段的数据为空，无法识别"),
    PASSWORD_NOT_EXIST("04022", "密码不能为空."),
    USER_ID_IS_NULL("04023", "用户名不能为空."),
    TOKEN_EXPIRE_ERROR("04024", "Token已过期."),
    BAD_CREDENTIALS("04025", "用户验证失败"),
    ERROR_CASE_NUMBER("04026", "案例编号重复"),
    ERROR_CASE_NUMBER2("04027", "案例编号,案例名,设备编号不能为空"),
    ERROR_RULE_NUMBER1("04028", "规则名称,案例名,规则描述不能为空"),
    ERROR_RULE_NUMBER2("04029", "规则编号重复"),
    ERROR_RULE_NUMBER3("04030", "故障征兆和故障结论不允许为空"),
    USER_ENABLE_DELETE("05000", "该用户组无法被删除"),
    ROLE_NAME_EXIST("05001", "角色名已存在");

    private RestResponseCode(String code, String label) {
        if (code == null || label == null) {
            throw new NullPointerException();
        }

        this.code = code;
        this.label = label;
    }

    public static RestResponseCode getByCode(String code) {
        if (code == null) {
            return null;
        }

        RestResponseCode[] instances = RestResponseCode.values();
        for (RestResponseCode i : instances) {
            if (i.getCode().equals(code)) {
                return i;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String code;
    private String label;
}
