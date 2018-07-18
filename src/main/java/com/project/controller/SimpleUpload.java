package com.project.controller;

import java.io.IOException;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class SimpleUpload {
	// 设置好账号的ACCESS_KEY和SECRET_KEY
	String ACCESS_KEY = "填写你的AccessKey"; // 这两个登录七牛 账号里面可以找到
	String SECRET_KEY = "填写你的SecretKey";

	// 要上传的空间
	String bucketname = "test"; // 填写新建的那个存储空间对象的名称
	// 上传到七牛后保存的文件名
	String key = "01.jpg";
	// 上传文件的路径
	String FilePath = "d:\\01.jpg"; // 本地要上传文件路径

	// 密钥配置
	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	// 创建上传对象
	UploadManager uploadManager = new UploadManager();

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public String getUpToken() {
		return auth.uploadToken(bucketname);
	}

	// 普通上传
	public void upload() throws IOException {
		try {
			// 调用put方法上传
			Response res = uploadManager.put(FilePath, key, getUpToken());
			// 打印返回的信息

			System.out.println(res.isOK());

			System.out.println(res.bodyString());
		} catch (QiniuException e) {
			Response r = e.response;
			// 请求失败时打印的异常的信息
			System.out.println(r.toString());
			try {
				// 响应的文本信息
				System.out.println(r.bodyString());
			} catch (QiniuException e1) {
				// ignore
			}
		}
	}

	public static void main(String args[]) throws IOException {
		new SimpleUpload().upload();
	}
}