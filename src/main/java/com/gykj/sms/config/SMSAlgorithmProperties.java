package com.gykj.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 22:42
 */
@ConfigurationProperties(prefix = "sms.algorithm")
public class SMSAlgorithmProperties {
    private String user;

    private String pass;



    private String sign;

    private String sendType;//custom k1

    private String contentUrl;

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "SMSAlgorithmProperties{" +
                "user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", sign='" + sign + '\'' +
                ", sendType='" + sendType + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
