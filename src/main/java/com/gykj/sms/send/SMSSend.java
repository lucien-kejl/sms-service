package com.gykj.sms.send;

import com.alibaba.fastjson.JSONObject;
import com.gykj.sms.config.SMSProperties;


import com.gykj.sms.context.SMSContext;
import com.gykj.sms.context.SMSProcess;
import com.gykj.sms.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;


/**
 * 过程：1.策略选择哪个供应商对应的处理类需要处理
 *         1.1 在 handle构建成发送的处理数据
 *         1.1.1 需要构建出 templateJson 的json,内容是 以手机号为key,value对应的content
 *         1.2 在send中 发送短信
 *         1.2.1 需要在返回数据中带上请求参数的reqin和reqout信息
*        2. reqin和reqout的数据都放到了结果中的第一个来保留数据
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 9:40
 */

@Service("smsSend")
public class SMSSend {

    protected  static Logger logger = LoggerFactory.getLogger(SMSSend.class);

    @Autowired
    SMSProperties smsProperties;

    @Autowired
    SMSContext smsContext;

    public  List<SysSmsRecord> send(@Validated SMSRequestBean smsRequestBean){
        logger.info(smsProperties.toString());

        int mock_status = 0;
        int sendstatus = 0;

        List<SysSmsRecord> smsResponseList = new ArrayList<SysSmsRecord>();

        if (smsProperties.isMockOn()){ //mock 数据直接返回
            mock_status = 1;
            for (int i = 0; i <smsRequestBean.getMobiles().size() ; i++) {
                SysSmsRecord  sysSmsRecord = new SysSmsRecord();
                sysSmsRecord.setMode(smsRequestBean.getSmsMode());
                sysSmsRecord.setVendor(smsRequestBean.getSmsVendor());
                sysSmsRecord.setPhone(smsRequestBean.getMobiles().get(i));
                sysSmsRecord.setContent(smsRequestBean.getContent());
                sysSmsRecord.setNote(smsRequestBean.getNote());
                sysSmsRecord.setMockStatus(mock_status);
                sysSmsRecord.setStatus(sendstatus);
                sysSmsRecord.setCreateTime(new Date());
                smsResponseList.add(sysSmsRecord);
            }
            return  smsResponseList;
        }

        SMSProcess smsProcess = smsContext.getSMSProcess(smsRequestBean.getSmsVendor());
        JSONObject handle = smsProcess.handle(smsRequestBean);
        JSONObject templateJson = handle.getJSONObject("templateJson");
        ResponseBean send = smsProcess.send(handle);

        if (!send.getRetCode().equals("0")){
            sendstatus = 1;

        }
        for (int i = 0; i <smsRequestBean.getMobiles().size() ; i++) {
            SysSmsRecord  sysSmsRecord = new SysSmsRecord();
            sysSmsRecord.setMode(smsRequestBean.getSmsMode());
            sysSmsRecord.setVendor(smsRequestBean.getSmsVendor());
            sysSmsRecord.setPhone(smsRequestBean.getMobiles().get(i));
            if(smsRequestBean.getMobiles().size()>1){
                sysSmsRecord.setType(2);
            }
            if(StringUtils.isEmpty(smsRequestBean.getTemplateId())){
                sysSmsRecord.setContent(smsRequestBean.getContent());
            }else{
                sysSmsRecord.setTemplateId(smsRequestBean.getTemplateId());
                sysSmsRecord.setContent(templateJson.getString(smsRequestBean.getMobiles().get(i)));
            }
            sysSmsRecord.setNote(smsRequestBean.getNote());
            sysSmsRecord.setMockStatus(mock_status);
            sysSmsRecord.setStatus(sendstatus);
            sysSmsRecord.setCreateTime(new Date());
            if (i==0){// 将发送请求和应答信息放到了第一条中没必要都存
                JSONObject jsonObject =  (JSONObject) send.getData();
                sysSmsRecord.setReqIn(jsonObject.getString("reqin"));
                sysSmsRecord.setReqOut(jsonObject.getString("reqout"));
            }
            smsResponseList.add(sysSmsRecord);
        }
        return smsResponseList;
    }


    /**
     * 增加场景-移动专用-模板场景对多个手机号
     * @param
     * @return
     */
    public  List<SysSmsRecord> sendSameContentWithTemplate(@Validated SMSRequestBean2 smsRequestBean2){
        logger.info(smsProperties.toString());
        Map<String,List<SMSRequestBean.ParamBean>> requetParam = new HashMap<String,List<SMSRequestBean.ParamBean>>();
        List<SMSRequestBean.ParamBean> paramBeans = new ArrayList<SMSRequestBean.ParamBean>();
        for (int i = 0; i < smsRequestBean2.getParams().size(); i++) {
            SMSRequestBean.ParamBean paramBean = new SMSRequestBean.ParamBean();
            paramBean.setValue(smsRequestBean2.getParams().get(i));
            paramBeans.add(paramBean);
        }
        requetParam.put("*",paramBeans);
        SMSRequestBean smsRequestBean = new SMSRequestBean();
        smsRequestBean.setTemplateId(smsRequestBean2.getTemplateId());
        smsRequestBean.setParamBeans(requetParam);
        smsRequestBean.setNote(smsRequestBean2.getNote());
        smsRequestBean.setMobiles(smsRequestBean2.getMobiles());
        smsRequestBean.setSmsMode(smsRequestBean2.getSmsMode());
        smsRequestBean.setSmsVendor(smsRequestBean2.getSmsMode());

       return send(smsRequestBean);
    }
}
