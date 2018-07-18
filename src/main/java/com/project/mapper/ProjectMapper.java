package com.project.mapper;

import java.util.List;
import java.util.Map;

import com.project.entity.Project;

public interface ProjectMapper {

	/**
	 * 登录
	 */
	Project login(String username);

	/**
	 * 查询
	 */
	List<Map<String, Object>> select(Map<String, Object> map);

	/*
	 * 查询总条数
	 */
	int countselect(Map<String, Object> map);

	/**
	 * 删除
	 */
	int delete(int id);

	/**
	 * 新增
	 */
	int insert(Map<String, Object> map);

	/**
	 * 修改
	 */
	int update(Map<String, Object> map);

	/**
	 * 修改前id查询
	 */
	Project updateselect(int id);

}
