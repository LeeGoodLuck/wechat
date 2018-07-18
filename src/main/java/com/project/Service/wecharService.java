package com.project.Service;

import com.project.entity.PayAppuser;

public interface wecharService {
	/**
	 * 根据区域拿到相应的地区id
	 * @param parkingNo
	 * @return
	 */
	String queryParkingNo(String areaName);
	
	/**
	 * 根据openId查询用户信息
	 * @param openId
	 * @return
	 */
	PayAppuser queryUserByOpenId(String openId);
	
	/**
	 * 修改app用户信息
	 * @param appUser
	 * @return
	 */
	boolean updateAppUser(PayAppuser appUser);
	
	/**
	 * 增加app用户
	 * @param appUser
	 * @return
	 */
	boolean addAppUser(PayAppuser appUser);
}
