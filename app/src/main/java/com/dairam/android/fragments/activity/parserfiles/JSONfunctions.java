package com.dairam.android.fragments.activity.parserfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONfunctions {

	public static JSONObject getJSONfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// Download JSON data from URL
		try {
			
			/*HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);*/  
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// Convert response to string
		try {
			/*BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);*/
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF_8"), 8);
			//iso-8859-1
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("result in Json Fn.",result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
			e.printStackTrace();
		}

		return jArray;
	}
	
	public static JSONObject getJsonObjectMethod(String getURL)
	{
		JSONObject responseJSONObj = null;
		try {
			HttpParams params1 = new BasicHttpParams();
			HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params1, "UTF-8");
			params1.setBooleanParameter("http.protocol.expect-continue", false);
			HttpClient httpclient = new DefaultHttpClient(params1);
			
			Log.e("getURL", getURL);
			//HttpPost httppost = new HttpPost(getURL);
			HttpGet httppost = new HttpGet(getURL);
			
			try {
				HttpResponse http_response = httpclient.execute(httppost);

				HttpEntity entity = http_response.getEntity();
				String jsonText = EntityUtils.toString(entity, HTTP.UTF_8);
				Log.e("Json Fn. REspo", jsonText);
				responseJSONObj = new JSONObject(jsonText);

			} catch (Exception e) 
			{
				Log.e("JsonFunctions","Error in Creating JSON");
				e.printStackTrace();
			}		
			
		} catch (Exception e) 
		{
			Log.e("JsonFunctions","Error in HTTP");
			e.printStackTrace();
		}
		return responseJSONObj;
	}
}
