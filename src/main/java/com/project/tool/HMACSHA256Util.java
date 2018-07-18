package com.project.tool;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.project.constans.wecharConstans;

public class HMACSHA256Util {

	public static String hmac_SHA256(String message, String secret) {
		String hash = "";
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
			byte[] bytes = sha256_HMAC.doFinal(message.getBytes(wecharConstans.CHARSETS));
			hash = new HexBinaryAdapter().marshal(bytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}
}
