package com.gykj.sms.entity;

import java.util.Date;

public class SysSmsRecord {
    private Long id;

    private String phone;

    private Integer vendor;

    private Integer type;

    private Integer mode;

    private String templateId;

    private String content;

    private Integer status;

    private String note;

    private Integer mockStatus;

    private String reqIn;

    private String reqOut;

    private Date createTime;

    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getVendor() {
        return vendor;
    }

    public void setVendor(Integer vendor) {
        this.vendor = vendor;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getMockStatus() {
        return mockStatus;
    }

    public void setMockStatus(Integer mockStatus) {
        this.mockStatus = mockStatus;
    }

    public String getReqIn() {
        return reqIn;
    }

    public void setReqIn(String reqIn) {
        this.reqIn = reqIn == null ? null : reqIn.trim();
    }

    public String getReqOut() {
        return reqOut;
    }

    public void setReqOut(String reqOut) {
        this.reqOut = reqOut == null ? null : reqOut.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}