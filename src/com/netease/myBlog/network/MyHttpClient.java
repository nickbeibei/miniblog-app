package com.netease.myBlog.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class MyHttpClient {
	private final String BASEURL = "http://test.easyread.163.com";
	public final String TAG = "jcdebug";
	static CookieStore mCookieStore = new BasicCookieStore();
	/**
	 * 
	 * @param url
	 * @param head
	 * @param params
	 * @return
	 */
	public Map<String, String> myHttpPost(String url, Map<String, String> headers, Map<String, String> urlParams,
			Map<String, String> params) {
		Map<String, String> rsMap = new HashMap<String, String>();
		String rs = "";
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		
		
		DefaultHttpClient client = new DefaultHttpClient();
		client.getCookieStore().getCookies();
		String uri = urlParams(url, urlParams);
		HttpPost post = new HttpPost(uri);
		
		
		// header 认证信息
		if (headers != null) {
			Iterator<String> keys = headers.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = headers.get(key);
				post.setHeader(key, value);
			}
		}
		try {
			// post 参数
			if (params != null) {
				Iterator<String> keys = params.keySet().iterator();
				String key = keys.next();
				nvps.add(new BasicNameValuePair(key, params.get(key)));
				
			}
//			StringEntity aaa = new StringEntity(json.toString);
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF_8"));
			// 请求
			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			rsMap.put("code", "" + code);
			if (code == 200) {
				HttpEntity entity = response.getEntity();
				reader = new BufferedReader(new InputStreamReader(
						entity.getContent(), "UTF-8"));

				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				rs = buffer.toString();
				rsMap.put("responseBody", rs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return rsMap;
	}

	public Map<String, String> myHttpGet(String url, Map<String, String> headers,
			Map<String, String> urlParams) {
		Map<String, String> rsMap = new HashMap<String, String>();
		String rs = "";
		String uri = urlParams(url, urlParams);
		Log.d(TAG, "uri=" + uri);
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(uri);
		Log.d(TAG, "uri=" + uri);
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		

		if (headers != null) {
			Log.d(TAG, "headers");
			Iterator<String> keys = headers.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = headers.get(key);
				get.setHeader(key, value);
			}
		}

		// 请求
		try {
			HttpResponse response = client.execute(get);
			int code = response.getStatusLine().getStatusCode();
			Log.d(TAG, "code=" + code);
			rsMap.put("code", "" + code);
			HttpEntity entity = response.getEntity();
			reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));

			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			rs = buffer.toString();
			rsMap.put("result", rs);
		} catch (Exception e) {
			Log.d(TAG, e.toString());
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

		return rsMap;
	}

	public String urlParams(String url, Map<String, String> urlParams) {
		StringBuffer uri = new StringBuffer(BASEURL + url);
		if (urlParams != null) {
			uri.append("?");
			Iterator<String> keys = urlParams.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = urlParams.get(key);
				uri.append("&");
				uri.append(key + "=" + value);
			}
		}
		String uri2 = uri.toString();
		uri2 = uri2.replace("?&", "?");
		return uri2;
	}

	public static void main(String[] args) {
		MyHttpClient hClient = new MyHttpClient();
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> urlParams = new HashMap<String, String>();
		Map<String, String> rsMap = new HashMap<String, String>();
		headers.put("Authorization", "Basic amlhbmdjb25nMDAwMkAxNjMuY29tOnFhMTIzNA==");
		urlParams.put("query", "%E7%BE%8E%E5%A5%B3");
		String url = "/searchbook2.atom?";
		rsMap = hClient.myHttpGet(url, headers, urlParams);
	}

	public String getTest() {
		String res = "";
		try {  
            //得到HttpClient对象  
            HttpClient getClient = new DefaultHttpClient();  
            //得到HttpGet对象  
            HttpGet request = new HttpGet("https://www.baidu.com/");  
            //客户端使用GET方式执行请教，获得服务器端的回应response  
            HttpResponse response = getClient.execute(request);  
            //判断请求是否成功    
            if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
                Log.i(TAG, "请求服务器端成功");  
                //获得输入流  
                InputStream  inStrem = response.getEntity().getContent();  
                int result = inStrem.read();  
                while (result != -1){  
                    System.out.print((char)result);  
                    res += (char)result;
                    result = inStrem.read();  
                }  
                //关闭输入流  
                inStrem.close();
                
            }else {  
                Log.i(TAG, "请求服务器端失败");  
            }             
        } catch (Exception e) {  
            // TODO Auto-generated catch block 
        	Log.d(TAG, e.toString());
            e.printStackTrace();  
        }  
		return res;
    }  
}
