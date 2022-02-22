package com.gykj.sms.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gykj.sms.config.SMSAlgorithmProperties;
import com.gykj.sms.constant.SmsErrorConstant;
import com.gykj.sms.exception.CommonJsonException;
import com.gykj.sms.util.HttpClientUtils;
import com.gykj.sms.entity.ResponseBean;
import com.gykj.sms.entity.SMSRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:52
 */
@Service("algorithmSMSProcess")
public class AlgorithmSMSProcess implements SMSProcess {

    protected  static Logger logger = LoggerFactory.getLogger(AliCloudSMSProcess.class);

    @Autowired
    SMSAlgorithmProperties smsAlgorithmProperties;


    @Override
    public ResponseBean send(JSONObject param) {
        HttpClientUtils hc = HttpClientUtils.HC;
        JSONObject retJson = new JSONObject();
        List<JSONObject> data = (List<JSONObject>) param.get("data");
        String url = param.getString("url");
        AtomicReference<String> finalRes = new AtomicReference<String>("");
        AtomicReference<String> errorInfo = new AtomicReference<String>("");
        HttpEntity httpEntity = hc.setHeaders(data, null);
            try {
                retJson.put("reqin", JSON.toJSONString(httpEntity));
                ResponseEntity<String> res = hc.send(url, HttpMethod.POST, httpEntity);
                String resBody = res.getBody();
                retJson.put("reqout", JSON.toJSONString(resBody));
                logger.info("短信url：{},状态：{},响应信息：{}" ,url,res.getStatusCode(),resBody);
                if (res.getStatusCode().value()==200){
                    JSONObject resResult = JSONObject.parseObject(resBody);
                    if (0 != resResult.getJSONArray("data").getJSONObject(0).getInteger("code")) {
                        errorInfo.set(SmsErrorConstant.SMS_RESPONSE_ERROR);
                    }
                }else {
                    errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
                }
            } catch (Exception e) {
                logger.error("短信厂商：{}，发送异常：{}","Algorithm",e);
                errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
            }

           // if (1==requestBean.getNotifyType()){
                if (!StringUtils.isEmpty(errorInfo.get())){
                    finalRes.set(errorInfo.get());
                }
        //    }

//            if (super.logJson.size()>0){
//                SmsLogRecord smsLogRecord = (SmsLogRecord)logJson.get(js.getString("mobile"));
//                try {
//                    smsLogRecord.setReqContent(JSONObject.toJSONString(httpEntity));
//                } catch (Exception e) {
//                    logger.error("请求体信息解析异常：{}",e);
//                }
//                smsLogRecord.setCreateTime(new Date());
//                if (StringUtils.isEmpty(errorInfo.get())){
//                    smsLogRecord.setSmsStatus(1); //成功
//                }else {
//                    smsLogRecord.setSmsStatus(2); //失败
//                }
//            }

        if (StringUtils.isEmpty(finalRes.get())){
            return  new ResponseBean("0","成功",retJson);
        }else {
            return  new ResponseBean("-1",finalRes.get(),retJson);
        }
    }

    @Override
    public JSONObject handle(SMSRequestBean requestBean) {
        logger.info(smsAlgorithmProperties.toString());

        int smsMode = requestBean.getSmsMode(); //1-文本 2-模板
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        if (1==smsMode){
            for (int x=0;x < requestBean.getMobiles().size();x++){
                JSONObject data = new JSONObject();
                data.put("clientid", smsAlgorithmProperties.getUser());
                data.put("password", smsAlgorithmProperties.getPass());
                data.put("smstype", smsAlgorithmProperties.getSendType());
                data.put("content", smsAlgorithmProperties.getSign()+requestBean.getContent());
                String phone = (String)requestBean.getMobiles().get(x);
                data.put("mobile",phone);
                jsonList.add(data);
                jsonObject.put(phone,requestBean.getContent());
            }

        }else {
            throw new CommonJsonException("-1", SmsErrorConstant.SMS_REQUEST_ERROR2);
        }
        JSONObject result = new JSONObject();
        result.put("data",jsonList);
        result.put("url",smsAlgorithmProperties.getContentUrl());
        result.put("templateJson",jsonObject);
        logger.info("短信厂商：{},发封装参数：{}，url:{}","Algorithm",jsonList,smsAlgorithmProperties.getContentUrl());
        return result;
    }
}
