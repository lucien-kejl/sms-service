package com.gykj.sms.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gykj.sms.config.SMSTianyiCloudProperties;
import com.gykj.sms.constant.SmsErrorConstant;
import com.gykj.sms.exception.CommonJsonException;
import com.gykj.sms.util.AccessTokenHandler;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:50
 */
@Service("tianyiCloudSMSProcess")
public class TianyiCloudSMSProcess implements SMSProcess {

    protected  static Logger logger = LoggerFactory.getLogger(TianyiCloudSMSProcess.class);

    @Autowired
    SMSTianyiCloudProperties smsTianyiCloudProperties;


    @Override
    public ResponseBean send(JSONObject param) {
        String url = param.getString("url");
        List<JSONObject> data = (List<JSONObject>) param.get("data");

        JSONObject retJson = new JSONObject();

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type","application/json");
        headerMap.put("appId", smsTianyiCloudProperties.getUser());
        headerMap.put("appSecret",smsTianyiCloudProperties.getPass());

        AtomicReference<String> finalRes = new AtomicReference<String>("");

            String token="";
            //是否需要token
            if(smsTianyiCloudProperties.isNeedToken()){
                token = "?access_token=" + new AccessTokenHandler().getAccessToken(smsTianyiCloudProperties.getTokenUrl(),
                        smsTianyiCloudProperties.getUser(),  smsTianyiCloudProperties.getPass(),
                        smsTianyiCloudProperties.getCustomK1());
            }
            String tokenUrl = url + token;
            AtomicReference<String> errorInfo = new AtomicReference<String>("");
            HttpClientUtils hc = HttpClientUtils.HC;

            HttpEntity httpEntity = hc.setHeaders(data, headerMap);
             retJson.put("reqin", JSON.toJSONString(httpEntity));
            try {
                ResponseEntity<String> res = hc.send(tokenUrl, HttpMethod.POST, httpEntity);
                String resBody = res.getBody();
                retJson.put("reqout", resBody);

                logger.info("短信url：{},状态：{},响应信息：{}" ,url,res.getStatusCode(),resBody);
                if (res.getStatusCode().value()==200){
                    JSONObject resResult = JSONObject.parseObject(resBody);
                    if (!resResult.containsKey("code") || "200".equals(resResult.getString("code"))) {
                        errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
                    }
                }else {
                    errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
                }
            } catch (Exception e) {
                logger.error("短信厂商：{}，发送异常：{}","tianyicloud",e);
                errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
            }


                if (!StringUtils.isEmpty(errorInfo.get())){
                    finalRes.set(errorInfo.get());
                }


//            if (super.logJson.size()>0){
//                SmsLogRecord smsLogRecord = (SmsLogRecord)logJson.get(js.getString("receiverNumber"));
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
        logger.info(smsTianyiCloudProperties.toString());

        int smsMode = requestBean.getSmsMode(); //1-文本 2-模板
        List<String> mobiles = requestBean.getMobiles();

        JSONObject templateJson = new JSONObject();

        String url = null;
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        if (1==smsMode){
            throw new CommonJsonException("-1", SmsErrorConstant.SMS_REQUEST_ERROR3);
        }else {
            if (1== requestBean.getMobiles().size()){
                JSONObject jsonObject = new JSONObject();
                String phone = (String)requestBean.getMobiles().get(0);
                jsonObject.put("id",requestBean.getTemplateId());
                jsonObject.put("receiverNumber",phone);

                JSONObject reJson = new JSONObject();
                if(requestBean.getParamBeans()!=null && requestBean.getParamBeans().size()>0){
                    for (int i = 0; i < requestBean.getParamBeans().get(mobiles.get(0)).size(); i++) {
                        reJson.put(requestBean.getParamBeans().get(mobiles.get(0)).get(i).getKey(),
                                requestBean.getParamBeans().get(mobiles.get(0)).get(i).getValue());
                    }
                    jsonObject.put("templateParam", JSONObject.toJSONString(reJson));
                    templateJson.put(mobiles.get(0),reJson);
                }
                jsonList.add(jsonObject);
            }else {
                if (requestBean.getMobiles().size()!=requestBean.getParamBeans().size()){
                    throw new CommonJsonException("-1",SmsErrorConstant.SMS_REQUEST_ERROR5);
                }

                if(requestBean!=null && requestBean.getParamBeans().size()>0){
                    JSONArray jsonArray = new JSONArray();
                    for (int x=0;x < requestBean.getParamBeans().size();x++){
                        JSONObject jsonObject = new JSONObject();
                        for (int i = 0; i <requestBean.getParamBeans().get(mobiles.get(x)).size() ; i++) {
                            jsonObject.put(requestBean.getParamBeans().get(mobiles.get(x)).get(i).getKey(),
                                    requestBean.getParamBeans().get(mobiles.get(x)).get(i).getValue() );
                        }
                        jsonArray.add(jsonObject);
                        String phone = (String)requestBean.getMobiles().get(x);
                        templateJson.put(phone,jsonObject);
                        jsonObject.put("id",requestBean.getTemplateId());
                        jsonObject.put("receiverNumber",requestBean.getMobiles().get(x));
                        jsonObject.put("templateParam",jsonArray);
                        jsonList.add(jsonObject);
                    }
                }


            }
            if("16".equals(requestBean.getTemplateId())){
                //验证码短信发送地址
                url = smsTianyiCloudProperties.getTemplateUrl();
            }else if("8".equals(requestBean.getTemplateId())||
                    "9".equals(requestBean.getTemplateId())||
                    "10".equals(requestBean.getTemplateId())||
                    "11".equals(requestBean.getTemplateId())||
                    "12".equals(requestBean.getTemplateId())){
                //
                url = smsTianyiCloudProperties.getSendSMSUrl();
            }else {
                throw new CommonJsonException("-1", SmsErrorConstant.SMS_REQUEST_ERROR6);
            }

        }
        JSONObject result = new JSONObject();
        result.put("data",jsonList);
        result.put("url",url);
        result.put("templateJson",templateJson);
        logger.info("短信厂商：{},发封装参数：{}，，url:{}","tianyicloud",jsonList,url);
        return result;
    }
}
