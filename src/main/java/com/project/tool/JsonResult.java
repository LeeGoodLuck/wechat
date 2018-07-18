package com.project.tool;

import java.io.Serializable;

/**
 * 返回json结果模板 0-表示成功 ，其他-如果错误返回相应错误码
 * @author Administrator
 *
 */
public class JsonResult implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean success;//是否成功
	
	private Object code;  //状态码
	
	private String message; //错误描述
	
	private Object data;  //返回数据

	public static JsonResult newInstance() {
		return new JsonResult();
	}
	
	public void success() {
		this.success=true;
		this.code="0";
	}
	
	public void failed(Object code) {
		this.success=false;
		this.code=code;
	}
	public void failed(Object code,String message) {
		this.success=false;
		this.message=message;
		this.code=code;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	public Object getData() {
		return data;
	}
	public void setDate(Object data) {
		this.data=data;
	}
	public Object getCode() {
		return code;
	}
	public void setCode(Object code) {
		this.code=code;
	}
}
