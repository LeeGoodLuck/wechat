package com.project.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import com.project.constans.wecharConstans;

import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Map;

public class SignUtil {

	/**
	 * 生成订单号(时间日期+五位随机数)
	 */
	public static String createOrderNo() {
		SimpleDateFormat simpl = new SimpleDateFormat("yyyyMMddHHmmssSSS");// 精确到毫秒
		String dataStr = simpl.format(new Date());
		Random random = new Random();
		int nextInt = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机五位数
		return dataStr + nextInt;
	}

	/**
	 * 生成随机字符串
	 */
	public static String createNonceStr() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 创建签名
	 */
	public static String creatSign(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Object, Object>> es = parameters.entrySet(); // 所有参与传参的参数按accsii排序(升序)
		Iterator<Entry<Object, Object>> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + wecharConstans.KEY);
		String sign = HMACSHA256Util.hmac_SHA256(sb.toString(), wecharConstans.KEY).toUpperCase();
		return sign;
	}
}
