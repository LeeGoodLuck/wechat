package com.project.Service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Service.ProjectService;
import com.project.entity.Project;
import com.project.mapper.ProjectMapper;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectmapper;

	@Override
	public Project login(String username) {
		return projectmapper.login(username);
	}

	@Override
	public List<Map<String, Object>> select(Map<String, Object> map) {
		return projectmapper.select(map);
	}

	@Override
	public int countselect(Map<String, Object> map) {
		return projectmapper.countselect(map);
	}

	@Override
	public int delete(int id) {
		return projectmapper.delete(id);
	}

	@Override
	public int insert(Map<String, Object> map) {
		return projectmapper.insert(map);
	}

	@Override
	public int update(Map<String, Object> map) {
		return projectmapper.update(map);
	}

	@Override
	public Project updateselect(int id) {
		return projectmapper.updateselect(id);
	}

}
