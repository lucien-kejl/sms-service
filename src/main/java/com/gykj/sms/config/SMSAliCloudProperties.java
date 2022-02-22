package com.gykj.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 19:36
 */
@ConfigurationProperties(prefix = "sms.ali")
public class SMSAliCloudProperties {


    private String user;

    private String pass;

    private String sysVersion;

    private String sysDomain;

    private String regionId;

    private String templateSign;

    private String profile;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTemplateSign() {
        return templateSign;
    }

    public void setTemplateSign(String templateSign) {
        this.templateSign = templateSign;
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


    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }




    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getSysDomain() {
        return sysDomain;
    }

    public void setSysDomain(String sysDomain) {
        this.sysDomain = sysDomain;
    }

    @Override
    public String toString() {
        return "SMSAliCloudProperties{" +
                "user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", sysVersion='" + sysVersion + '\'' +
                ", sysDomain='" + sysDomain + '\'' +
                ", regionId='" + regionId + '\'' +
                ", templateSign='" + templateSign + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
