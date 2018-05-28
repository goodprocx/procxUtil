package com.procx.util;


import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;




import java.net.URLDecoder;
import java.util.HashMap;

public class HttpClientUtil {
 
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);   
	
	public static void main(String[] args) {
		HashMap map = new HashMap();
		map.put("xm", "陈向");
		map.put("gmsfhm", "500236198706043419");
		JSONObject jsonParam = JSONObject.fromObject(map);
	}
	
	/**
	 * post请求
	 * @param url         url地址
	 * @param jsonParam     参数
	 * @return
	 */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
    	JSONObject jsonResult = null;
        try {
        	//post请求返回结果
        	HttpClient httpClient =HttpClients.createDefault();
        	HttpPost method = new HttpPost(url);
            if (null != jsonParam) {
            	method.addHeader("Content-Type", "application/json;charset=utf-8");
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (Exception e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
 
 
    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
        	HttpClient client = HttpClients.createDefault();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (Exception e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
}