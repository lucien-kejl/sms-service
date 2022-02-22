package com.gykj.sms.constant;

/**
 * 短信发送错误提示信息
 */
public class SmsErrorConstant {

    public static final String SMS_REQUEST_ERROR = SmsSuffix.SMS_REQUEST_ERROR.getValue();
    public static final String SMS_REQUEST_ERROR2 = SmsSuffix.SMS_REQUEST_ERROR2.getValue();
    public static final String SMS_REQUEST_ERROR3 = SmsSuffix.SMS_REQUEST_ERROR3.getValue();
    public static final String SMS_REQUEST_ERROR4 = SmsSuffix.SMS_REQUEST_ERROR4.getValue();
    public static final String SMS_REQUEST_ERROR5 = SmsSuffix.SMS_REQUEST_ERROR5.getValue();
    public static final String SMS_REQUEST_ERROR6 = SmsSuffix.SMS_REQUEST_ERROR6.getValue();
    public static final String SMS_RESPONSE_ERROR = SmsSuffix.SMS_RESPONSE_ERROR.getValue();
    public static final String SMS_RESPONSE_ERROR2 = SmsSuffix.SMS_RESPONSE_ERROR2.getValue();
    private enum SmsSuffix {

        SMS_REQUEST_ERROR("供应商接口异常，未返回相应报文"),
        SMS_REQUEST_ERROR2("该厂商不支持模板短信发送"),
        SMS_REQUEST_ERROR3("该厂商不支持文本短信发送"),
        SMS_REQUEST_ERROR4("短信模板（文本）格式有误"),
        SMS_REQUEST_ERROR5("手机号码个数和参数组个数应一致"),
        SMS_REQUEST_ERROR6("未知的模板ID"),
        SMS_RESPONSE_ERROR("供应商发送短信失败"),
        SMS_RESPONSE_ERROR2("供应商接口异常，报文格式错误"),


        ;

        private String value;

        SmsSuffix(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
