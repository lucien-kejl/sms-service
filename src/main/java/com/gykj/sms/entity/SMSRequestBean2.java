package com.gykj.sms.entity;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 17:07
 */
public class SMSRequestBean2 {



    //供应上标识
    @Range(min=1,max=4,message="请选择合适的供应商发送")
    private int smsVendor;

    //短信模式 1-文本 2-模板
    @Range(min=1,max=2,message="请选择合适发送模式")
    private int smsMode;

    //发送的手机号列表
    @NotEmpty
    private List<String> mobiles;

    //短信模板id
    private String templateId;


    //短信的参数列表
    @NotEmpty
    private List<String> params;

    //发送的自定义内容
    private String note;



    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getSmsVendor() {
        return smsVendor;
    }

    public void setSmsVendor(int smsVendor) {
        this.smsVendor = smsVendor;
    }

    public int getSmsMode() {
        return smsMode;
    }

    public void setSmsMode(int smsMode) {
        this.smsMode = smsMode;
    }

    public List<String> getMobiles() {
        return mobiles;
    }

    public void setMobiles(List<String> mobiles) {
        this.mobiles = mobiles;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }




}
