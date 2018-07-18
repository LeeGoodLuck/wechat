package com.project.entity;

public class PayAppuser implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id; // 主键

	private String mobile; // 手机号

	private String HeadPortrait; // 头像

	private String appellation; // 用户名

	private String password; // 密码

	private String salt; // 盐值

	private Integer userMoney; // 账号余额：单位分

	private double userjianglijin; // 用户奖励金

	private int cumulativeRecharge; // 累积充值

	private int userStatus; // 用户状态

	private int weiTing; // 违停次数

	private String createTime; // 创建时间
	
	private String openId; 		// 微信唯一编号
	
	private int isWheter; 	// 是否分享过

	private String areacode; // 最近登录地

	private String proName;
	
	private String cityName;
	
	private String areaName;
	
	private String carNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeadPortrait() {
		return HeadPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		HeadPortrait = headPortrait;
	}

	public String getAppellation() {
		return appellation;
	}

	public void setAppellation(String appellation) {
		this.appellation = appellation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Integer getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(Integer userMoney) {
		this.userMoney = userMoney;
	}

	public double getUserjianglijin() {
		return userjianglijin;
	}

	public void setUserjianglijin(double userjianglijin) {
		this.userjianglijin = userjianglijin;
	}

	public int getCumulativeRecharge() {
		return cumulativeRecharge;
	}

	public void setCumulativeRecharge(int cumulativeRecharge) {
		this.cumulativeRecharge = cumulativeRecharge;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public int getWeiTing() {
		return weiTing;
	}

	public void setWeiTing(int weiTing) {
		this.weiTing = weiTing;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public int getIsWheter() {
		return isWheter;
	}

	public void setIsWheter(int isWheter) {
		this.isWheter = isWheter;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

}
