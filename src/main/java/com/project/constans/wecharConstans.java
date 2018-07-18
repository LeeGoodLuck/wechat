package com.project.constans;

public class wecharConstans {

	// 编码格式
	public static final String CHARSETS = "UTF-8";

	// 公众账号ID_appID
	public static final String WECHAT_SMALL_PROGRAM_APPID = "wx2dbcb59954d8cb76";
	// 我的appid
	// public static final String WECHAT_SMALL_PROGRAM_APPID="wx2ca8a5f908db7263";
	// 我的
	// public static final String
	// WECHAT_SMALL_PROGRAM_APPID="785327afde5fbf288ae0094bdd4d743c";

	// 商户号
	public static final String MCH_ID = "1502583971";

	// 签名类型(签名类型，目前支持HMAC-SHA256和MD5)
	public static final String SIGN_TYPE = "HMAC-SHA256";

	// 支付通知地址
	public static final String NOTIFY_URL = "http://www.weixin.qq.com/wxpay/pay.php";

	// 支付统一下单接口
	public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	// 商户API安全秘钥
	public static final String KEY = "shanghaiyiwei2018051901502583971";

	public static final String WECHAT_SMALL_PROGRAM_APPSECRET = "f1cbbca22f602b68d8c83b52ca162bab";

	/** 填写为 authorization_code */
	public static final String GRANT_TYPE = "authorization_code";

	public static final String JSCODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

	/**
	 * 登录状态过期期间
	 */
	public static final long LOGIN_OUT_TIME = 1800;

	/** 登录失败 */
	public static final int LOGIN_ERROR = 10001;

}
