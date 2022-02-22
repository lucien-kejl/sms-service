package com.gykj.sms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HTTP请求类
 * @author LiHong
 */
public class HttpInvoker {
	
	private static Logger logger = LoggerFactory.getLogger(HttpInvoker.class);
	
	/**
	 * GET请求
	 * @param getUrl
	 * @throws IOException
	 * @return 提取HTTP响应报文包体，以字符串形式返回
	 */
	public static String httpGet(String getUrl, Map<String, String> getHeaders) throws IOException {
		URL getURL = new URL(getUrl);
		HttpURLConnection connection = (HttpURLConnection) getURL.openConnection();

        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		if(getHeaders != null) {
			for(String pKey : getHeaders.keySet()) {
				connection.setRequestProperty(pKey, getHeaders.get(pKey));
			}
		}
		connection.connect();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sbStr = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) { 
			sbStr.append(line); 
		} 
		bufferedReader.close();
		connection.disconnect(); 
		return new String(sbStr.toString().getBytes(),"utf-8");
	}
	
	/**
	 * POST请求
	 * @param postUrl
	 * @param postHeaders
	 * @param postEntity
	 * @throws IOException
	 * @return 提取HTTP响应报文包体，以字符串形式返回
	 */
	public static String httpPost(String postUrl,Map<String, String> postHeaders, String postEntity) throws IOException {
		
		URL postURL = new URL(postUrl); 
		HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection(); 
		httpURLConnection.setDoOutput(true);                 
		httpURLConnection.setDoInput(true); 
		httpURLConnection.setRequestMethod("POST"); 
		httpURLConnection.setUseCaches(false); 
		httpURLConnection.setInstanceFollowRedirects(true); 
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		if(postHeaders != null) {
			for(String pKey : postHeaders.keySet()) {
				httpURLConnection.setRequestProperty(pKey, postHeaders.get(pKey));
			}
		}
		if(postEntity != null) {
			DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream()); 
			out.writeBytes(postEntity); 
			out.flush(); 
			out.close(); // flush and close 
		}
		//connection.connect(); 
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())); 
		StringBuilder sbStr = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sbStr.append(line); 
		} 
		bufferedReader.close();
		httpURLConnection.disconnect(); 
		return new String(sbStr.toString().getBytes(),"utf-8");
	} 
	/**
	 * POST请求 ,解决中文乱码问题
	 * @param postUrl
	 * @param postHeaders
	 * @param postEntity
	 * @throws IOException
	 * @return 提取HTTP响应报文包体，以字符串形式返回
	 */
	public static String httpPost1(String postUrl,Map<String, String> postHeaders, String postEntity) throws IOException {
		
		URL postURL = new URL(postUrl); 
		HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection(); 
		httpURLConnection.setDoOutput(true);                 
		httpURLConnection.setDoInput(true); 
		httpURLConnection.setRequestMethod("POST"); 
		httpURLConnection.setUseCaches(false); 
		httpURLConnection.setInstanceFollowRedirects(true); 
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		StringBuilder sbStr = new StringBuilder();
		if(postHeaders != null) {
			for(String pKey : postHeaders.keySet()) {
				httpURLConnection.setRequestProperty(pKey, postHeaders.get(pKey));
			}
		}
		if(postEntity != null) {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8"));
			out.println(postEntity);  
			out.close();  
			BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection  
			        .getInputStream()));  
			  
			String inputLine; 
			while ((inputLine = in.readLine()) != null) {  
				sbStr.append(inputLine);  
			}  
			in.close();  
		}
		httpURLConnection.disconnect(); 
		return new String(sbStr.toString().getBytes(),"utf-8");
	}


	/**
	 * post by JSON
	 * 
	 * @param postUrl 		not null
	 * @param postParams	not null
	 * @return null if failed,result from remote
	 */
	public static String post(String postUrl, String postParams) {
		if(postUrl == null) {
			return null;
		}
		if(postParams == null) {postParams = "";}
		StringBuffer result = new StringBuffer();
		OutputStreamWriter out = null;
		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(postUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			
			// use the OutPutStream to call 
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(postParams);
			out.flush();
			
			if(conn.getResponseCode() == 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line;
				while((line = in.readLine()) != null) {
					result.append(line);
				}
				String retStr = result.toString();
				if(retStr == null) {retStr = "";}
				
				logger.info("[post] Result : " + retStr);
				return retStr;
			}else {
				logger.info("[post] Result : ResponseCode=" + conn.getResponseCode() + ",ResponseMessage=" + conn.getResponseMessage());
				return null;
			}
		} catch (Exception e) {
			logger.error("[post]", e);
			return null;
		} finally {
			try {
				if(out != null) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("[post]", e);
				return null;
			}
			try {
				if(in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("[post]", e);
				return null;
			}
			try {
				if(conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				logger.error("[post]", e);
				return null;
			}
		}
	}
	
	public static String postFormUrlEncoded(String postUrl, Map<String, String> headers, String postParams) {
		if(postUrl == null) {
			return null;
		}
		if(postParams == null) {postParams = "";}
		StringBuffer result = new StringBuffer();
		OutputStreamWriter out = null;
		BufferedReader in = null;
		HttpURLConnection conn = null;
        try {
            URL url = new URL(postUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                	conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.connect();
            
            // use the OutPutStream to call 
 			out = new OutputStreamWriter(conn.getOutputStream());
 			out.write(postParams);
 			out.flush();

            if(conn.getResponseCode() == 200) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line;
				while((line = in.readLine()) != null) {
					result.append(line);
				}
				String retStr = result.toString();
				if(retStr == null) {retStr = "";}
				
				logger.info("[postFormUrlEncoded] Result : " + retStr);
				return retStr;
			}else {
				logger.info("[postFormUrlEncoded] Result : ResponseCode=" + conn.getResponseCode() + ",ResponseMessage=" + conn.getResponseMessage());
				return null;
			}
        } catch (Exception e) {
        	logger.error("[postFormUrlEncoded]", e);
        	return null;
        } finally {
        	try {
				if(out != null) {
					out.close();
				}
			} catch (Exception e) {
				logger.error("[postFormUrlEncoded]", e);
				return null;
			}
			try {
				if(in != null) {
					in.close();
				}
			} catch (Exception e) {
				logger.error("[postFormUrlEncoded]", e);
				return null;
			}
			try {
				if(conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				logger.error("[postFormUrlEncoded]", e);
				return null;
			}
        }
    }


}