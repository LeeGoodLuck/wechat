package com.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.entity.Project;
import com.project.mapper.ProjectMapper;
import com.project.tool.JsonResult;
import com.project.tool.MD5Util;
import com.project.tool.PageBean;

@RequestMapping("/admin")
@RestController
public class ProjectController {

	private static final Logger LOGGER = Logger.getLogger(ProjectController.class);

	@Autowired
	private ProjectMapper projectmapper;

	/**
	 * 新增
	 * 
	 * @return
	 */
	@RequestMapping("/insert")
	public JsonResult insert(HttpServletRequest request) {
		JsonResult json = JsonResult.newInstance();
		// 获取用户名、密码、性别、年龄
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		// 取得盐
		String salt = UUID.randomUUID().toString();
		MD5Util md5 = new MD5Util(salt, "sha-256");
		String newpassword = md5.encode(password);
		LOGGER.info("加盐后的密码=" + newpassword);
		Map<String, Object> map = new HashMap<>();
		// 存到map中
		map.put("username", username);
		map.put("password", newpassword);
		map.put("salt", salt);
		map.put("sex", sex);
		map.put("age", age);
		int count = projectmapper.insert(map);
		if (count > 0) {
			json.setDate(200);
			json.setMessage("新增成功");
			json.success();
		} else {
			json.setDate(500);
			json.setMessage("新增失败");
			json.failed("操作失败");
		}
		return json;
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	@RequestMapping("/login")
	public JsonResult login(HttpServletRequest request) {
		JsonResult json = JsonResult.newInstance();
		// 获取用户名密码
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		// 判断是否为空
		if (username != "" && !username.equals("") && password != "" && !password.equals("")) {
			Project project = projectmapper.login(username);
			// 获取到盐
			String salt = project.getSalt();
			MD5Util md5 = new MD5Util(salt, "sha-256");
			// 加密
			String newpassword = md5.encode(password);
			// 判断加密后的密码和数据库的密码是否一致
			if (newpassword.equals(project.getPassword())) {
				json.setDate(200);
				json.setMessage("登录成功");
				json.success();
			} else {
				json.setDate(500);
				json.setMessage("用户名或者密码错误");
				json.failed("登录失败");
			}
		} else {
			json.setDate(500);
			json.setMessage("请输入用户名或密码");
			json.failed("登录失败");
		}
		return json;
	}

	@RequestMapping("/select")
	public JsonResult select(HttpServletRequest request) {
		JsonResult json = JsonResult.newInstance();
		// 获取用户名
		String username = request.getParameter("username");
		PageBean pb = new PageBean();
		Map<String, Object> map = new HashMap<>();
		pb.setPc(getPc(request));
		// 模糊搜索
		map.put("username", username);
		// 分页查询
		map.put("pageIndex", (pb.getPc() - 1) * pb.getPs());
		map.put("pageSize", pb.getPs());
		pb.setTr(projectmapper.countselect(map));
		List<Map<String, Object>> listmap = projectmapper.select(map);
		pb.setMap(listmap);
		Logger.getLogger("pb=" + pb);
		if (listmap.size() != 0) {
			json.setDate(pb);
			json.setMessage("操作成功");
			json.success();
		} else if (listmap.size() == 0) {
			json.setDate("");
			json.setMessage("无数据");
			json.success();
		} else {
			json.setDate(500);
			json.setMessage("操作失败");
			json.failed("访问失败");
		}
		return json;
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public JsonResult delete(int id) {
		JsonResult json = JsonResult.newInstance();
		int count = projectmapper.delete(id);
		if (count > 0) {
			json.setDate(200);
			json.setMessage("删除成功");
			json.success();
		} else {
			json.setDate(500);
			json.setMessage("删除失败");
			json.failed("操作失败");
		}
		return json;
	}

	/**
	 * 修改前查询
	 */
	@RequestMapping("/selectupdate")
	public JsonResult selectupdate(int id) {
		JsonResult json = JsonResult.newInstance();
		Project project = projectmapper.updateselect(id);
		json.setDate(project);
		json.setMessage("操作成功");
		json.success();
		return json;
	}

	@RequestMapping("/update")
	public JsonResult update(HttpServletRequest request) {
		JsonResult json = JsonResult.newInstance();
		String id = request.getParameter("id");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String salt = UUID.randomUUID().toString();
		MD5Util md5 = new MD5Util(salt, "sha-256");
		String newpassword = md5.encode(password);
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("password", newpassword);
		map.put("salt", salt);
		map.put("sex", sex);
		map.put("age", age);
		map.put("id", id);
		int count = projectmapper.update(map);
		if (count > 0) {
			json.setDate(200);
			json.setMessage("修改成功");
			json.success();
		} else {
			json.setDate(500);
			json.setMessage("修改失败");
			json.failed("操作失败");
		}
		return json;
	}

	/**
	 * 获取当前页
	 */
	private int getPc(HttpServletRequest request) {
		String value = request.getParameter("pageIndex");
		return value == null || value.trim().isEmpty() ? 1 : Integer.parseInt(value);
	}
}
