package com.project.tool;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("unused")
public class PageBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public PageBean() {
	}

	private int pc; // 当前页

	private int tp; // 总页数

	private int tr; // 总记录数

	private int ps = 10; // 每一页显示多少条数据

	private List<Map<String, Object>> map;

	public List<Map<String, Object>> getMap() {
		return map;
	}

	public void setMap(List<Map<String, Object>> map) {
		this.map = map;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getTp() {
		int tp = tr / ps;
		return tr % ps == 0 ? tp : tp + 1;
	}

	public void setTp(int tp) {
		this.tp = tp;
	}

	public int getTr() {
		return tr;
	}

	public void setTr(int tr) {
		this.tr = tr;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

}
