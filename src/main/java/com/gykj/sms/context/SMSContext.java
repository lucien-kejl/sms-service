package com.gykj.sms.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:47
 */
@Service("smsContext")
public class SMSContext {

    @Autowired
    SMSProcess chinaMobileSMSProcess;
    @Autowired
    SMSProcess aliCloudSMSProcess;
    @Autowired
    SMSProcess tianyiCloudSMSProcess;
    @Autowired
    SMSProcess algorithmSMSProcess;
    public  SMSProcess getSMSProcess(int smsVendor){
        switch (smsVendor){
            case 1:
                return  chinaMobileSMSProcess;
            case 2:
                return aliCloudSMSProcess;
            case 3:
                return tianyiCloudSMSProcess;
            case 4:
                return algorithmSMSProcess;
            default:
                return null;

        }

    }
}
