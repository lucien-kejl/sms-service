package com.gykj.sms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenHandler {
	private static Logger logger = LoggerFactory.getLogger(AccessTokenHandler.class);

	public String getAccessToken(String accessTokenUrl, String appId, String appSecret, String grantType) {
		String access_token = "";
		try {
			String postEntity = "app_id=" + appId + "&app_secret=" + appSecret + "&grant_type=" + grantType;
			logger.info("getAccessToken()->postEntity={}", postEntity);
			String responseStr = HttpInvoker.httpPost(accessTokenUrl, null, postEntity);
			JSONObject responseJSONObject = JSON.parseObject(responseStr);
			if (responseJSONObject.containsKey("access_token")) {
				access_token = String.valueOf(responseJSONObject.get("access_token"));
			}
			logger.info("access_token={}", access_token);
		} catch (Exception e) {
			logger.error("getAccessToken()->error", e);
		}

		return access_token;
	}
}
