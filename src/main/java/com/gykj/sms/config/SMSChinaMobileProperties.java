package com.gykj.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 9:44
 */
@ConfigurationProperties(prefix = "sms.chinamobile")
public class SMSChinaMobileProperties {


    private String ecName;
    private String apId;
    private String contentSign;
    private String templateSign;
    private String addSerial;
    private String user;
    private String pass;
    private String templateUrl;
    private String contentUrl;

    public String getContentSign() {
        return contentSign;
    }

    public void setContentSign(String contentSign) {
        this.contentSign = contentSign;
    }

    public String getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(String templateSign) {
        this.templateSign = templateSign;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }


    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
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

    @Override
    public String toString() {
        return "SMSChinaMobileProperties{" +
                "ecName='" + ecName + '\'' +
                ", apId='" + apId + '\'' +
                ", contentSign='" + contentSign + '\'' +
                ", templateSign='" + templateSign + '\'' +
                ", addSerial='" + addSerial + '\'' +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", templateUrl='" + templateUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
