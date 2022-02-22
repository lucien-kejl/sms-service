package com.gykj.sms.context;

import com.alibaba.fastjson.JSONObject;
import com.gykj.sms.entity.ResponseBean;
import com.gykj.sms.entity.SMSRequestBean;

/**
 * @author kejl
 * @version 1.0.0
 * @date 2022/2/11 16:35
 */
public interface SMSProcess {



    /**
     * 将请求参数和响应参数进行输出
     * @param jsonObject
     * @return
     */
    ResponseBean send(JSONObject jsonObject);

    /**
     * templateJson 这个是模板情况下手机号为key value为content的值
     *
     * @param requestBean
     * @return
     */
    JSONObject handle(SMSRequestBean requestBean);



}
