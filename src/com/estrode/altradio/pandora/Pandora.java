package com.estrode.altradio.pandora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.android.volley.RequestQueue;
import com.squareup.okhttp.OkHttpClient;

public class Pandora {
	private final String DEFAULT_CLIENT_ID = "android-generic";
	
	private Map<String, String> clientKeys = new HashMap<String, String>();
	
	private JSONObject partnerKeys;
	
	private String partnerId;
	private String userId;
	private String partnerAuthToken;
	private String userAuthToken;
	private int timeOffset;
	
	private OkHttpClient httpClient = new OkHttpClient();

	public Pandora() {
		clientKeys.put("encryptKey", "6#26FRL$ZWD");
		clientKeys.put("decryptKey", "R=U!LH$O2B#");
		clientKeys.put("username", "android");
		clientKeys.put("password", "AC7IBG09A3DTSYM4R41UJWL07VLN8JI7");
		clientKeys.put("deviceModel", "android-generic");
		clientKeys.put("rpcUrl", "://tuner.pandora.com/services/json/?");
		clientKeys.put("version", "5");
		
		partnerKeys = new JSONObject();
		try {
			partnerKeys.put("username", "android");
			partnerKeys.put("password", "AC7IBG09A3DTSYM4R41UJWL07VLN8JI7");
			partnerKeys.put("deviceModel", "android-generic");
			partnerKeys.put("version", "5");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject sendJsonRequest(String method, JSONObject args, Boolean https, Boolean blowfish) {
		ArrayList<String> urlArgs = new ArrayList<String>();
		if (partnerId != null) {
			urlArgs.add("partner_id=" + partnerId);
		}
		if (userId != null) {
			urlArgs.add("user_id=" + userId);
		}
		if (userAuthToken != null) {
			urlArgs.add("auth_token=" + userAuthToken);
		} else if (partnerAuthToken != null) {
			urlArgs.add("auth_token=" + partnerAuthToken);
		}
		urlArgs.add("method=" + method);
		
		String protocol = (https) ? "https" : "http";
		String url = protocol + clientKeys.get("rpcUrl") + StringUtils.join(urlArgs.toArray(), "&");
		
//		if (timeOffset != 0) {
//			try {
//				args.put("syncTime", (int) (System.currentTimeMillis() / 1000L)+timeOffset);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		if (userAuthToken != null) {
			try {
				args.put("userAuthToken", userAuthToken);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (partnerAuthToken != null) {
			try {
				args.put("partnerAuthToken", partnerAuthToken);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String jsonData = args.toString();
		
		if (blowfish) {
			try {
				jsonData = encrypt(jsonData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String responseBody = null;
		responseBody = post(url, jsonData);
		
		JSONObject responseBodyJson = null;
		try {
			responseBodyJson = new JSONObject(responseBody);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return responseBodyJson;
	}
	
	public void login(String username, String password) {
		partnerId = null;
		userId = null;
		partnerAuthToken = null;
		userAuthToken = null;
		timeOffset = 0;
		
		JSONObject partner = sendJsonRequest("auth.partnerLogin", partnerKeys, true, false);
		JSONObject result = partner.optJSONObject("result");
		partnerId = result.optString("partnerId");
		partnerAuthToken = result.optString("partnerAuthToken");
//		int pandoraTime = 0;
//		try {
//			pandoraTime = Integer.parseInt(decrypt(partner.optString("syncTime")));
//		} catch (NumberFormatException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		timeOffset = pandoraTime - (int) (System.currentTimeMillis() / 1000L);
		
		JSONObject userInfo = new JSONObject();
		try {
		userInfo.put("username", username);
		userInfo.put("password", password);
		userInfo.put("loginType", "user");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject user = sendJsonRequest("auth.userLogin", userInfo, true, true);
		//result = user.optJSONObject("result");
		//userId = result.optString("userId");
		//userAuthToken = result.optString("userAuthToken");
		
		System.out.println(user.toString());
		//TODO fetch stations list upon login
	}

	//encrypt data using blowfish algorithm
	public String encrypt(String data)throws Exception {
		SecretKeySpec key = new SecretKeySpec(clientKeys.get("encryptKey").getBytes("UTF8"), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return new String(Hex.encodeHex(cipher.doFinal(data.getBytes("UTF8"))));
	}

	//decrypt JSON returned from pandora api using blowfish algorithm
	public String decrypt(String data)throws Exception {
		byte[] encryptedData = Hex.decodeHex(data.toCharArray());
		SecretKeySpec key = new SecretKeySpec(clientKeys.get("decryptKey").getBytes("UTF8"), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(encryptedData);
		return new String(Arrays.copyOfRange(decrypted, 4, 14)); //this decrypt function is only used for server time which has 4 junk bytes
	}

	String post(String url, String body) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new StringEntity(body, "UTF8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		HttpResponse resp = null;
		HttpClient httpclient = new DefaultHttpClient();
		try {
			resp = httpclient.execute(post);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpEntity entity = resp.getEntity();
		String responseString = null;
		try {
			responseString = EntityUtils.toString(entity, "UTF8");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(responseString);
		return responseString;
	}
	
}
