package com.gykj.sms.constant;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/17 9:18
 */
public enum SMSVendor {

    ChinaMobile("中国移动", 1), AliCloud("阿里云", 2), TianyiCloud("天翼云", 3), Algorithm("算法商城", 4);
    // 成员变量
    private String name;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;
    // 构造方法
    private SMSVendor(String name, int index) {
        this.name = name;
        this.index = index;
    }


    //覆盖方法
    @Override
    public String toString() {
        return this.index+"_"+this.name;
    }
}
