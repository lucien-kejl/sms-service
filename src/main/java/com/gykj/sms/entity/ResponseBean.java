package com.gykj.sms.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/14 10:14
 */
public class ResponseBean implements Serializable {

    public static final long serialVersionUID = 1L;

    private String retCode;

    private String retMsg;

    private Object data;

    public ResponseBean(String retCode,String retMsg,Object data){
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = data;
    }

    public ResponseBean(String retCode,String retMsg){
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = new JSONObject();
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static long getSerialVersionUID(){
        return serialVersionUID;
    }

}
