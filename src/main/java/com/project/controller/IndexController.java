package com.project.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;


public class IndexController {

	@RequestMapping("/")
	public String index(Model model) throws IOException {

		return "/index";
	}

	@RequestMapping("/upload")
	public String upload(HttpServletRequest request) throws Exception {
		// 判断enctype属性是否为multipart/form-data
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
			throw new IllegalArgumentException("上传内容不是有效的multipart/form-data类型.");

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List<?> items = upload.parseRequest(request);

		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();

			if (item.isFormField()) {
				// 如果是普通表单字段
				String name = item.getFieldName();
				String value = item.getString();
				// ...
			} else {
				// 如果是文件字段
				String fieldName = item.getFieldName();
				String fileName = item.getName();
				String contentType = item.getContentType();
				boolean isInMemory = item.isInMemory();
				long sizeInBytes = item.getSize();
				// ...

				// 上传到远程服务器
				HashMap<String, FileItem> files = new HashMap<String, FileItem>();
				files.put(fileName, item);
				uploadToQiNiuYun(files);
			}
		}
		return "redirect:/";
	}

	private void uploadToQiNiuYun(HashMap<String, FileItem> files) throws IOException {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		// ...其他参数参考类注释
		// 华东 Zone.zone0()
		// 华北 Zone.zone1()
		// 华南 Zone.zone2()
		// 北美 Zone.zoneNa0()

		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		String accessKey = "CE-NyriEo276P4tAPqvgOcf1jYnRByh_CoueF7ra";// 这里请替换成自己的AK
		String secretKey = "pkyPYTRrm7CKX5gKCsFeYbesCB1TNMdfvFtFQn0l";// 这里请替换成自己的SK
		String bucket = "image";// 这里请替换成自己的bucket--空间名

		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;

		Iterator iter = files.entrySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			i++;
			Map.Entry entry = (Map.Entry) iter.next();
			String fileName = (String) entry.getKey();
			FileItem val = (FileItem) entry.getValue();
			InputStream inputStream = val.getInputStream();
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[600]; // buff用于存放循环读取的临时数据
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			byte[] uploadBytes = swapStream.toByteArray(); // uploadBytes 为转换之后的结果
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
				Response response = uploadManager.put(uploadBytes, key, upToken);
				// 解析上传成功的结果
				DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
				System.out.println(putRet.key);
				System.out.println(putRet.hash);
			} catch (QiniuException ex) {
				Response r = ex.response;
				System.err.println(r.toString());
				try {
					System.err.println(r.bodyString());
				} catch (QiniuException ex2) {
					// ignore
				}
			}
		}
		
	}
}
