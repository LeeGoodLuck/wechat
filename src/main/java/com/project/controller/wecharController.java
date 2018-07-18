package com.project.controller;

import java.util.HashMap;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.project.constans.wecharConstans;
import com.project.tool.HttpsUtil;
import com.project.tool.JsonResult;
import com.project.tool.SignUtil;
import com.project.tool.XMLUtil;

@RequestMapping("/wechar")
@RestController
public class wecharController {


	public static final Logger LOGGER = Logger.getLogger(wecharController.class);

	@RequestMapping("/unifiedorder")
	public JsonResult unifiedorder(HttpServletRequest request, String userid) {
		JsonResult json = JsonResult.newInstance();

		String nocestr = SignUtil.createNonceStr();
		// 商户订单号
		String out_trade_no = SignUtil.createOrderNo();
		SortedMap<Object, Object> params = new TreeMap<Object, Object>();
		// 公众账号ID
		params.put("appid", wecharConstans.WECHAT_SMALL_PROGRAM_APPID);
		// 商户号
		params.put("mch_id", wecharConstans.MCH_ID);
		// 随机字符串
		params.put("nonce_str", nocestr);
		// 签名类型
		params.put("sign_type", wecharConstans.SIGN_TYPE);
		// 商品描述
		params.put("body", "金额支付");
		// 商户订单号
		params.put("out_trade_no", out_trade_no);
		// 货币类型
		params.put("fee_type", "CNY");
		// 标价金额
		params.put("total_fee", 1);
		// 终端IP
		params.put("spbill_create_ip", request.getRemoteHost());
		// 通知地址
		params.put("notify_url", wecharConstans.NOTIFY_URL);
		// 交易类型
		params.put("trade_type", "JSAPI");
		// 用户标识
		params.put("openid", 1);
		/**
		 * 调用微信统一支付
		 */
		String xmlStr = HttpsUtil.postXml(wecharConstans.PAY_URL, params);
		try {
			Map<String, Object> xml2map = XMLUtil.xml2map(xmlStr, false);
			LOGGER.info("统一下单返回的map集合=" + xml2map);
			// 返回状态码
			String returnCode = (String) xml2map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				// 业务结果
				String prepayId = (String) xml2map.get("prepay_id");
				LOGGER.info("prepayId=" + prepayId);
				/**
				 * 组合数据再次签名，返回支付参数
				 */
				// 生成支付签名，这个签名给微信支付的调用使用
				SortedMap<Object, Object> signmap = new TreeMap<Object, Object>();
				signmap.put("appId", wecharConstans.WECHAT_SMALL_PROGRAM_APPID);
				long timestamp = System.currentTimeMillis();
				signmap.put("timeStamp", timestamp + "");
				signmap.put("nonceStr", nocestr);
				signmap.put("package", "prepay_id=" + prepayId);
				signmap.put("signType", wecharConstans.SIGN_TYPE);
				// 再次签名
				String sign = SignUtil.creatSign(signmap);
				LOGGER.info("再次签名=" + sign);
				signmap.put("paySign", sign);
				signmap.put("out_trade_no", out_trade_no);
				signmap.remove("appId");
				// 返回5个支付参数和sign
				json.success();
				json.setDate(signmap);
			} else {
				String err_code = (String) xml2map.get("err_code");
				String err_code_des = (String) xml2map.get("err_code_des");
				json.failed(err_code, err_code_des);
				LOGGER.error(err_code_des);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 微信小程序登录接口
	 * 
	 * @return 登录状态 session值
	 */
	@RequestMapping(value = "/xiaoLogin", method = { RequestMethod.POST })
	@ResponseBody
	public JsonResult wxLogin(@RequestParam("code") String code, HttpServletRequest request) {
		JsonResult json = JsonResult.newInstance();
		/*
		 * 1. 使用登录凭证 code 获取 session_key 和 openid 2. 生成session值，以session为key，
		 * session_key+ openid为值，写入session存储
		 */
		// 使用登录凭证 code 获取 session_key 和 openid
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", wecharConstans.WECHAT_SMALL_PROGRAM_APPID);
		params.put("secret", wecharConstans.WECHAT_SMALL_PROGRAM_APPSECRET);
		params.put("js_code", code);
		params.put("grant_type", wecharConstans.GRANT_TYPE);
		String reponseStr = HttpsUtil.get(wecharConstans.JSCODE2SESSION_URL, params);
		JSONObject jsonObject = JSONObject.parseObject(reponseStr);
		System.out.println(jsonObject);
		String token = UUID.randomUUID().toString().replace("-", "");
		json.setDate(token);
		return json;
	}
}
