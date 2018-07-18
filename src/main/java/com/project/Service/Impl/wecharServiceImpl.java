package com.project.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Service.wecharService;
import com.project.entity.PayAppuser;
import com.project.mapper.wecharMapper;

@Service
@Transactional
public class wecharServiceImpl implements wecharService {

	@Autowired
	private wecharMapper wecharMapper;

	@Override
	public String queryParkingNo(String areaName) {
		return wecharMapper.queryParkingNo(areaName);
	}

	@Override
	public PayAppuser queryUserByOpenId(String openId) {
		return wecharMapper.queryUserByOpenId(openId);
	}

	@Override
	public boolean updateAppUser(PayAppuser appUser) {
		return wecharMapper.updateAppUser(appUser);
	}

	@Override
	public boolean addAppUser(PayAppuser appUser) {
		return wecharMapper.updateAppUser(appUser);
	}

}
