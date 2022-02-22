package com.gykj.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/15 8:46
 */
@ConfigurationProperties(prefix = "sms.conf")
public class SMSProperties {


    private boolean mockOn;

    public boolean isMockOn() {
        return mockOn;
    }

    public void setMockOn(boolean mockOn) {
        this.mockOn = mockOn;
    }


    @Override
    public String toString() {
        return "SMSProperties{" +
                "mockOn=" + mockOn +
                '}';
    }
}
