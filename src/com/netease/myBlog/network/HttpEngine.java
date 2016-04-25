package com.netease.myBlog.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EncodingUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.netease.myBlog.myStruct.GlobalParameters;

public class HttpEngine {
	static CookieStore mCookieStore = new BasicCookieStore();
	private static final int NET_TIMEOUT = 1 * 10 * 1000;
	private DefaultHttpClient mHttpClient;
	public final String TAG = "jcdebug";

	public HttpEngine() {
		mHttpClient = new DefaultHttpClient();
		mHttpClient.setCookieStore(mCookieStore);
		HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(),
				NET_TIMEOUT);
		HttpConnectionParams.setSoTimeout(mHttpClient.getParams(), NET_TIMEOUT);
	}

	public void getMethod(String url) {
		String uri = GlobalParameters.MiniBlogInterface.BASE_URL + url;
		HttpGet get = new HttpGet(uri);
		StringBuffer buffer = new StringBuffer();
		Map<String, String> rs = new HashMap<String, String>();
		int code = -1;
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Authorization", GlobalParameters.TOKEN);

		try {
			HttpResponse response = mHttpClient.execute(get);
			code = response.getStatusLine().getStatusCode();
			rs.put("code", "" + code);
			if (code == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				rs.put("body", buffer.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Bundle getMethodBundle(String url) {
		Bundle rs = new Bundle();
		String uri = GlobalParameters.MiniBlogInterface.BASE_URL + url;
		HttpGet get = new HttpGet(uri);
		StringBuffer buffer = new StringBuffer();
		int code = -1;
		get.setHeader("Content-Type", "application/json");
		get.setHeader("Authorization", GlobalParameters.TOKEN);
		try {
			HttpResponse response = mHttpClient.execute(get);
			code = response.getStatusLine().getStatusCode();
			rs.putInt("code", code);
			if (code == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					line = EncodingUtils.getString(line.getBytes(), "UTF-8"); 
					buffer.append(line);
				}
				rs.putString("body", buffer.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * post
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public Map<String, String> postMethod(String url, String params) {
		String uri = GlobalParameters.MiniBlogInterface.BASE_URL + url;
		Log.d("jcdebug", "url:" + uri);
		StringBuffer buffer = new StringBuffer();
		Map<String, String> rs = new HashMap<String, String>();
		HttpPost post = new HttpPost(uri);
		int code = -1;
		post.setHeader("Content-Type", "application/json");
		StringEntity sEntity;
		try {
			Log.d(TAG, "body:" + params);
			sEntity = new StringEntity(params);
			post.setEntity(sEntity);
			HttpResponse response = mHttpClient.execute(post);
			code = response.getStatusLine().getStatusCode();
			Log.d(TAG, "code:" + code);
			rs.put("code", "" + code);
			if (code == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				rs.put("body", buffer.toString());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return rs;
	}

	public Bundle postMethodBundle(String url, String params) {
		Bundle bundle = new Bundle();
		String uri = GlobalParameters.MiniBlogInterface.BASE_URL + url;
		Log.d(TAG, uri);
		Log.d(TAG, "Authorization" + GlobalParameters.TOKEN);
		StringBuffer buffer = new StringBuffer();
		Map<String, String> rs = new HashMap<String, String>();
		HttpPost post = new HttpPost(uri);
		int code = -1;
		post.setHeader("Content-Type", "application/json");
		post.setHeader("Authorization", GlobalParameters.TOKEN);
		StringEntity sEntity;
		try {
			Log.d(TAG, "postbody:" + params);
//			sEntity = new StringEntity(params);选择byteArrayEntity，规避乱码的问题
			ByteArrayEntity byteEntity = new ByteArrayEntity(params.getBytes());
			post.setEntity(byteEntity);
			HttpResponse response = mHttpClient.execute(post);
			code = response.getStatusLine().getStatusCode();
			rs.put("code", "" + code);
			bundle.putInt("code", code);
			if (code == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(), "UTF-8"));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				Log.d(TAG, buffer.toString());
				bundle.putString("body", buffer.toString());
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return bundle;
	}
	
	
	public Bitmap getMethodPicture(String uri) {
		Bitmap rs  = null;
		HttpGet get = new HttpGet(uri);
		try {
			HttpResponse response = mHttpClient.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				InputStream is = httpEntity.getContent();
				rs = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
//	public Bundle postMethodPicture(String url, Bitmap bitmap) {
//		Bundle rs = new Bundle();
//		Log.d(TAG, "start");
//		String uri = GlobalParameters.MiniBlogInterface.BASE_URL + url;
//		Log.d(TAG, "url:" + uri);
//		HttpPost post = new HttpPost(uri);
//		StringBuffer buffer = new StringBuffer();
//		int code = -1;
//		post.setHeader("Authorization", GlobalParameters.TOKEN);
//		Log.d(TAG, "bitmap == null:" + (bitmap == null));
//		
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			bitmap.compress(CompressFormat.JPEG, 100, bos);
//			byte[] data = bos.toByteArray();
//			ByteArrayEntity byteEntity = new ByteArrayEntity(data);
//			post.setEntity(byteEntity);
//			HttpResponse response = mHttpClient.execute(post);
//			Log.d(TAG, "why:" + code);
//			code = response.getStatusLine().getStatusCode();
//			Log.d(TAG, "code:" + code);
//			if (code == HttpStatus.SC_OK) {
//				HttpEntity entity = response.getEntity();
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(entity.getContent(), "UTF-8"));
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					buffer.append(line);
//				}
//				Log.d(TAG, buffer.toString());
//				rs.putString("body", buffer.toString());
//				Log.d(TAG, buffer.toString());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.d(TAG, e.toString());
//		}
//		
//		return rs;
//	}
	
	
	

}
