package com.estrode.altradio.pandora;

import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Pandora {
	
	private static String encryptKey = "6#26FRL$ZWD";
	private static String decryptKey = "R=U!LH$O2B#";

	public void Pandora() {
		
	}
	
	public JSONObject jsonCall(String method, Boolean https, Boolean blowfish) {
		return null;
	}

	//encrypt data using blowfish algorithm
	public static String encrypt(String data)throws Exception {
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("UTF8"), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return Hex.encodeHexString(cipher.doFinal(data.getBytes("UTF8")));
	}

	//decrypt JSON returned from pandora api using blowfish algorithm
	public static String decrypt(String data)throws Exception {
		byte[] encryptedData = Hex.decodeHex(data.toCharArray());
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("UTF8"), "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decrypted = cipher.doFinal(encryptedData);
		return new String(decrypted); 
	}
	
	
}
