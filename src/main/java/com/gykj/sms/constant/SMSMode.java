package com.gykj.sms.constant;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/17 9:35
 */
public enum SMSMode {

    TextMode("文本", 1), Template("阿里云", 2);
    private String name;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
    // 构造方法
    private SMSMode(String name, int index) {
        this.name = name;
        this.index = index;
    }


    //覆盖方法
    @Override
    public String toString() {
        return this.index+"_"+this.name;
    }
}
