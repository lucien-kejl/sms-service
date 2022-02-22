package com.gykj.sms.config;


import com.gykj.sms.context.*;
import com.gykj.sms.send.SMSSend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 9:41
 */
@Configuration
@ConditionalOnClass(SMSSend.class)
//@ConditionalOnProperty(prefix = "sms",value = "enable",matchIfMissing = true)
@EnableConfigurationProperties({SMSAliCloudProperties.class,SMSTianyiCloudProperties.class,
SMSAlgorithmProperties.class,SMSProperties.class,SMSChinaMobileProperties.class})
public class SMSAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SMSSend smsSend() {
        return new SMSSend();
    }

    @Bean
    @ConditionalOnMissingBean
    public ChinaMobileSMSProcess chinaMobileSMSProcess(){
        return new ChinaMobileSMSProcess();
    }

    @Bean
    @ConditionalOnMissingBean
    public AliCloudSMSProcess aliCloudSMSProcess(){
        return new AliCloudSMSProcess();
    }

    @Bean
    @ConditionalOnMissingBean
    public TianyiCloudSMSProcess tianyiCloudSMSProcess(){
        return new TianyiCloudSMSProcess();
    }
    @Bean
    @ConditionalOnMissingBean
    public AlgorithmSMSProcess algorithmSMSProcess(){
        return new AlgorithmSMSProcess();
    }

    @Bean
    @ConditionalOnMissingBean
    public SMSContext smsContext(){
        return new SMSContext();
    }

}
