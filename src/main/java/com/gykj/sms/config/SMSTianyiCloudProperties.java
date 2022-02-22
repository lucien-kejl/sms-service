package com.gykj.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 22:13
 */
@ConfigurationProperties(prefix = "sms.tianyicloud")
public class SMSTianyiCloudProperties {


    private String templateUrl;//templateUrl 1

    private String sendSMSUrl;//templateUrl 2

    private String tokenUrl;//templateUrl 0

    private String customK1;//custom 1

    private String user;

    private String pass;

    private boolean needToken;//custom k2

    public boolean isNeedToken() {
        return needToken;
    }

    public void setNeedToken(boolean needToken) {
        this.needToken = needToken;
    }

    public String getCustomK1() {
        return customK1;
    }

    public void setCustomK1(String customK1) {
        this.customK1 = customK1;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
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



    public String getSendSMSUrl() {
        return sendSMSUrl;
    }

    public void setSendSMSUrl(String sendSMSUrl) {
        this.sendSMSUrl = sendSMSUrl;
    }



    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    @Override
    public String toString() {
        return "SMSTianyiCloudProperties{" +
                "templateUrl='" + templateUrl + '\'' +
                ", sendSMSUrl='" + sendSMSUrl + '\'' +
                ", tokenUrl='" + tokenUrl + '\'' +
                ", customK1='" + customK1 + '\'' +
                ", user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", needToken=" + needToken +
                '}';
    }
}
