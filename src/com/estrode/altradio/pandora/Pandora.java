package com.estrode.altradio.pandora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Pandora {
	private final String DEFAULT_CLIENT_ID = "android-generic";
	
	private Map<String, String> clientKeys = new HashMap<String, String>();
	
	private String partnerId;
	private String userId;
	private String partnerAuthToken;
	private String userAuthToken;
	private int timeOffset;

	public void Pandora() {
		clientKeys.put("encryptKey", "6#26FRL$ZWD");
		clientKeys.put("decryptKey", "R=U!LH$O2B#");
		clientKeys.put("deviceModel", "android-generic");
		clientKeys.put("username", "android");
		clientKeys.put("password", "AC7IBG09A3DTSYM4R41UJWL07VLN8JI7");
		clientKeys.put("rpcUrl", "://tuner.pandora.com/services/json/?");
		clientKeys.put("version", "5");
	}
	
	public JSONObject sendJsonRequest(String method, JSONObject args, Boolean https, Boolean blowfish) {
		return null;
	}
	
	public void login(String username, String password) {
		this.partnerId = null;
		this.userId = null;
		this.partnerAuthToken = null;
		this.userAuthToken = null;
		this.timeOffset = 0;
		
		Map<String, String> partnerKeys = clientKeys;
		partnerKeys.remove("encryptKey");
		partnerKeys.remove("decryptKey");
		partnerKeys.remove("rpcUrl");
		
		JSONObject partner = sendJsonRequest("auth.partnerLogin", new JSONObject(partnerKeys), true, false);
		this.partnerId = partner.optString("partnerId");
		this.partnerAuthToken = partner.optString("partnerAuthToken");
		
		JSONObject userInfo = new JSONObject();
		try {
		userInfo.put("username", username);
		userInfo.put("password", password);
		userInfo.put("loginType", "user");
		} catch (JSONException e) {
			//TODO log the exception
		}
		
		JSONObject user = sendJsonRequest("auth.partnerLogin", userInfo, true, true);
		this.userId = user.optString("userId");
		this.userAuthToken = user.optString("userAuthToken");
		
		//TODO fetch stations list upon login
	}

	//encrypt data using blowfish algorithm
	public String encrypt(String data)throws Exception {
		SecretKeySpec key = new SecretKeySpec(clientKeys.get("encryptKey").getBytes("UTF8"), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Hex.encodeHexString(cipher.doFinal(data.getBytes("UTF8")));
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
	
	
}
