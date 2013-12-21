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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.android.volley.RequestQueue;
import com.squareup.okhttp.OkHttpClient;

public class Pandora {
	private final String DEFAULT_CLIENT_ID = "android-generic";
	
	private Map<String, String> clientKeys = new HashMap<String, String>();
	
	private String partnerId;
	private String userId;
	private String partnerAuthToken;
	private String userAuthToken;
	private int timeOffset;
	
	private OkHttpClient httpClient = new OkHttpClient();

	public Pandora() {
		clientKeys.put("encryptKey", "6#26FRL$ZWD");
		clientKeys.put("decryptKey", "R=U!LH$O2B#");
		clientKeys.put("deviceModel", "android-generic");
		clientKeys.put("username", "android");
		clientKeys.put("password", "AC7IBG09A3DTSYM4R41UJWL07VLN8JI7");
		clientKeys.put("rpcUrl", "://tuner.pandora.com/services/json/?");
		clientKeys.put("version", "5");
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
		
		try {
			responseBody = post(new URL(url), jsonData.getBytes("UTF8"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		Map<String, String> partnerKeys = new HashMap<String, String>(clientKeys);
		partnerKeys.remove("encryptKey");
		partnerKeys.remove("decryptKey");
		partnerKeys.remove("rpcUrl");
		
		JSONObject partner = sendJsonRequest("auth.partnerLogin", new JSONObject(partnerKeys), true, false);
		partnerId = partner.optString("partnerId");
		partnerAuthToken = partner.optString("partnerAuthToken");
		
		JSONObject userInfo = new JSONObject();
		try {
		userInfo.put("username", username);
		userInfo.put("password", password);
		userInfo.put("loginType", "user");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject user = sendJsonRequest("auth.partnerLogin", userInfo, true, true);
		userId = user.optString("userId");
		userAuthToken = user.optString("userAuthToken");
		
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
		return new String(decrypted); 
	}

	String post(URL url, byte[] body) throws IOException {
		HttpURLConnection connection = httpClient.open(url);
		OutputStream out = null;
		InputStream in = null;
		try {
			// Write the request.
			connection.setRequestMethod("POST");
			out = connection.getOutputStream();
			out.write(body);
			out.close();

			// Read the response.
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException("Unexpected HTTP response: "
						+ connection.getResponseCode() + " " + connection.getResponseMessage());
			}
			in = connection.getInputStream();
			return readFirstLine(in);
		} finally {
			// Clean up.
			if (out != null) out.close();
			if (in != null) in.close();
		}
	}

	String readFirstLine(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		return reader.readLine();
	}
	
	
}
