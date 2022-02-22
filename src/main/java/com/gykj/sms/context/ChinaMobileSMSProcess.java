package com.gykj.sms.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gykj.sms.config.SMSChinaMobileProperties;
import com.gykj.sms.constant.SmsErrorConstant;
import com.gykj.sms.entity.ResponseBean;
import com.gykj.sms.entity.SMSRequestBean;
import com.gykj.sms.entity.SMSResponse;
import com.gykj.sms.exception.CommonJsonException;
import com.gykj.sms.util.HttpClientUtils;
import com.gykj.sms.util.MakeMD5;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;



/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:49
 */

@Service("chinaMobileSMSProcess")
public class ChinaMobileSMSProcess implements SMSProcess {

    protected  static Logger logger = LoggerFactory.getLogger(ChinaMobileSMSProcess.class);

    @Autowired
    SMSChinaMobileProperties smsChinaMobileProperties;


    @Override
    public ResponseBean send(JSONObject param) {

        HttpClientUtils hc = HttpClientUtils.HC;
        String data = param.getString("data");
        String url = param.getString("url");
        AtomicReference<String> errorInfo = new AtomicReference<String>("");
        HttpEntity httpEntity = hc.setHeaders(data, null);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reqin", JSON.toJSONString(httpEntity));
            ResponseEntity<String> res = hc.send(url, HttpMethod.POST, httpEntity);
            String resBody = res.getBody();
            logger.info("短信url：{},状态：{},响应信息：{}" ,url,res.getStatusCode(),resBody);
            jsonObject.put("reqout", resBody);
            if (res.getStatusCode().value()==200){
                JSONObject resResult = JSONObject.parseObject(resBody);
                if (!resResult.getBoolean("success")){
                    errorInfo.set(SmsErrorConstant.SMS_RESPONSE_ERROR);
                }
            }else {
                errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
            }
        } catch (Exception e) {
            logger.error("短信厂商：{}，发送异常：{}","chinamobile",e);
            errorInfo.set(SmsErrorConstant.SMS_REQUEST_ERROR);
        }

        if (StringUtils.isEmpty(errorInfo.get())){
            return  new ResponseBean("0","成功",jsonObject);
        }else {
            return  new ResponseBean("-1",errorInfo.get(),jsonObject);
        }
    }

    /**
     * 参数：因为移动发送模板时只能固定的数组，所以就取 SMSRequestBean paramBeans 中的一个key的一个值
     * templateJson 这个是模板情况下手机号为key value为content的值
     *
     * @param smsRequestBean
     * @return
     */
    @Override
    public JSONObject handle(SMSRequestBean smsRequestBean) {
        logger.info(smsChinaMobileProperties.toString());

        JSONObject data = new JSONObject();
        JSONObject ret = new JSONObject();
        List<String> mobileList = smsRequestBean.getMobiles();
        StringBuilder stringBuilder = new StringBuilder(); //这个是移动为了构建加密使用的字符串
        String url;
        String sign;
        String mobiles = mobileList.stream().collect(Collectors.joining(","));
        String content = "";
        if(StringUtils.isEmpty(smsRequestBean.getTemplateId()) && smsRequestBean.getSmsMode()==1){//没有模板，直接发送的是短信的内容
            url = smsChinaMobileProperties.getContentUrl();
            sign = smsChinaMobileProperties.getContentSign();
            if(smsRequestBean.getParamBeans()==null || smsRequestBean.getParamBeans().size()==0){//如果有多个手机号，内容相同，一对多报文，直接写一个内容即可
                data.put("mobiles",mobiles);
                content = smsRequestBean.getContent();
                data.put("content",smsRequestBean.getContent());
                stringBuilder.append(smsChinaMobileProperties.getEcName()).append(smsChinaMobileProperties.getUser()).append(smsChinaMobileProperties.getPass()).
                        append(mobiles).append(content).append(sign).append(smsChinaMobileProperties.getAddSerial());
            } else{ //多对多场景：一个手机号对应一个内容
                if(smsRequestBean.getMobiles().size()!=smsRequestBean.getParamBeans().size()){
                   throw new CommonJsonException("-1","手机号与内容数量不相等");
                }
                Map<String,String> contentMap = new HashMap();
                for (int i = 0; i <smsRequestBean.getMobiles().size() ; i++) {
                    contentMap.put(mobileList.get(i),smsRequestBean.getParamBeans().get(mobileList.get(i)).get(0).getValue());
                }
                content = JSON.toJSONString(contentMap);
                data.put("content", content);
                data.put("mobiles","");
                stringBuilder.append(smsChinaMobileProperties.getEcName()).append(smsChinaMobileProperties.getUser()).append(smsChinaMobileProperties.getPass()).
                     append(content).append(sign).append(smsChinaMobileProperties.getAddSerial());
            }




        }else{//有模板
            data.put("templateId",smsRequestBean.getTemplateId());
            data.put("mobiles",mobiles);
            JSONArray jsonArray = new JSONArray();
            Set<String> strings = smsRequestBean.getParamBeans().keySet();
            JSONObject templateJson = new JSONObject();
                for (int i = 0; i <smsRequestBean.getParamBeans().get("*").size() ; i++) {
                    jsonArray.add(smsRequestBean.getParamBeans().get("*").get(i).getValue());
                }

                for (int i = 0; i <mobileList.size() ; i++) {
                    templateJson.put(mobileList.get(i),JSON.toJSONString(jsonArray));
                }
            sign = smsChinaMobileProperties.getTemplateSign();
            content =  JSON.toJSONString(jsonArray);
            data.put("params",jsonArray);
            ret.put("templateJson",templateJson);
            url = smsChinaMobileProperties.getTemplateUrl();
            stringBuilder.append(smsChinaMobileProperties.getEcName()).append(smsChinaMobileProperties.getUser()).append(smsChinaMobileProperties.getPass()).append(smsRequestBean.getTemplateId())
                    .append(mobiles).append(content).append(sign).append(smsChinaMobileProperties.getAddSerial());
        }

        data.put("ecName",smsChinaMobileProperties.getEcName());
        data.put("apId",smsChinaMobileProperties.getApId());
        data.put("sign",sign);
        data.put("addSerial",smsChinaMobileProperties.getAddSerial());
       // data.put("")
        logger.info("mac加密之前的信息："+stringBuilder.toString());
        String mac = MakeMD5.encryptToMD5(stringBuilder.toString());
        data.put("mac",mac);
        String base64Str = Base64.encodeBase64String(JSON.toJSONString(data).getBytes(Charset.forName("UTF-8")));
        ret.put("data",base64Str);
        ret.put("url",url);
        logger.info("短信厂商：{},封装参数：{}，base64字符串：{}，url:{}","chinamobile",data,base64Str,url);
        return ret;
    }
}
