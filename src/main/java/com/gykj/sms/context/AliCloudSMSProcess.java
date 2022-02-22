package com.gykj.sms.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.gykj.sms.config.SMSAliCloudProperties;
import com.gykj.sms.constant.SmsErrorConstant;
import com.gykj.sms.exception.CommonJsonException;
import com.gykj.sms.util.StringTools;
import com.gykj.sms.entity.ResponseBean;
import com.gykj.sms.entity.SMSRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:50
 */
@Service("aliCloudSMSProcess")
public class AliCloudSMSProcess implements SMSProcess {

    protected  static Logger logger = LoggerFactory.getLogger(AliCloudSMSProcess.class);

    @Autowired
    SMSAliCloudProperties smsAliCloudProperties;



    @Override
    public ResponseBean send(JSONObject param) {
        CommonRequest data = (CommonRequest) param.get("data");

        JSONObject retObject = new JSONObject();

        DefaultProfile profile = DefaultProfile.getProfile(smsAliCloudProperties.getRegionId(),
                smsAliCloudProperties.getUser(), smsAliCloudProperties.getPass());
        IAcsClient client = new DefaultAcsClient(profile);
        AtomicReference<String> errorInfo = new AtomicReference<String>("");

        try {
            retObject.put("reqin",   JSON.toJSONString(param));
            CommonResponse commonResponse = client.getCommonResponse(data);
            String resBody = StringTools.checkNull(commonResponse.getData());
            retObject.put("reqout",resBody);
            logger.info("短信厂商：{},响应信息：{}" ,"ali",resBody);
            if (resBody==null){
                errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
            }else {
                try {
                    JSONObject resResult = JSONObject.parseObject(resBody);
                    String code = StringTools.checkNull(resResult.getString("Code"));
                    String message = StringTools.checkNull(resResult.getString("Message"));
                    if(code == null || message == null) {
                        errorInfo.set(SmsErrorConstant.SMS_RESPONSE_ERROR2+"，未找到code,message");
                    }else {
                        if(!"OK".equalsIgnoreCase(code)) {
                            errorInfo.set(message);
                        }
                    }
                } catch (Exception e) {
                    logger.error("短信厂商：{}，格式转换异常：{}","ali",e);
                    errorInfo.set(SmsErrorConstant.SMS_RESPONSE_ERROR2);
                }
            }

        } catch (ClientException e) {
            logger.error("短信厂商：{}，发送异常：{}","ali",e.getMessage());
            errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
        }

        if (StringUtils.isEmpty(errorInfo.get())){
            return  new ResponseBean("0","成功",retObject);
        }else {
            return  new ResponseBean("-1",errorInfo.get(),retObject);
        }
    }

    @Override
    public JSONObject handle(SMSRequestBean requestBean) {
        logger.info(smsAliCloudProperties.toString());
        int smsMode = requestBean.getSmsMode(); //1-文本 2-模板
        CommonRequest request = new CommonRequest();
        JSONObject templateJson = new JSONObject();
        if (1==smsMode){
            throw new CommonJsonException("-1", SmsErrorConstant.SMS_REQUEST_ERROR3);
        }else {

            request.setSysMethod(MethodType.POST);
            request.setSysVersion(smsAliCloudProperties.getSysVersion());//版本
            request.setSysDomain(smsAliCloudProperties.getSysDomain());//域名
            request.putQueryParameter("RegionId", smsAliCloudProperties.getRegionId());//标识
            List<String> mobiles = requestBean.getMobiles();
            String sign = smsAliCloudProperties.getTemplateSign();
            if (1==requestBean.getMobiles().size()){
                request.setSysAction("SendSms");
                request.putQueryParameter("PhoneNumbers", mobiles.get(0));
                request.putQueryParameter("SignName", sign);
                request.putQueryParameter("TemplateCode",requestBean.getTemplateId());
                JSONObject jsonObject = new JSONObject();
                if(requestBean.getParamBeans()!=null && requestBean.getParamBeans().size()>0){
                    for (int i = 0; i < requestBean.getParamBeans().get(mobiles.get(0)).size(); i++) {
                        jsonObject.put(requestBean.getParamBeans().get(mobiles.get(0)).get(i).getKey(),
                                requestBean.getParamBeans().get(mobiles.get(0)).get(i).getValue());

                    }
                }
                templateJson.put(mobiles.get(0),JSONObject.toJSONString(jsonObject));

                request.putQueryParameter("TemplateParam",JSONObject.toJSONString(jsonObject));
            }else {
                if (requestBean.getParamBeans()!=null && mobiles.size()!=requestBean.getParamBeans().size()){
                    throw new CommonJsonException("-1",SmsErrorConstant.SMS_REQUEST_ERROR5);
                }
                JSONArray signArray = new JSONArray();

                mobiles.forEach(ph->{
                    signArray.add(sign);
                });
                request.setSysAction("SendBatchSms");
                request.putQueryParameter("PhoneNumberJson", JSONObject.toJSONString(mobiles));
                request.putQueryParameter("SignNameJson", signArray.toJSONString());
                request.putQueryParameter("TemplateCode", requestBean.getTemplateId());
                JSONArray jsonArray = new JSONArray();
                if(requestBean.getParamBeans()!=null && requestBean.getParamBeans().size()>0){
                    for (int i = 0; i < requestBean.getParamBeans().size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        for (int j = 0; j < requestBean.getParamBeans().get(mobiles.get(i)).size(); j++) {
                            jsonObject.put(requestBean.getParamBeans().get(mobiles.get(i)).get(j).getKey(),
                                    requestBean.getParamBeans().get(mobiles.get(i)).get(j).getValue());

                        }
                        templateJson.put(mobiles.get(i),jsonObject);
                        jsonArray.add(jsonObject);
                    }
                }
                request.putQueryParameter("TemplateParamJson",JSONObject.toJSONString(jsonArray) );
            }
        }
        JSONObject result = new JSONObject();
        result.put("data",request);
        result.put("templateJson",templateJson);
        logger.info("短信厂商：{},发封装参数：{}","ali",JSONObject.toJSONString(request));
        return result;
    }
}
