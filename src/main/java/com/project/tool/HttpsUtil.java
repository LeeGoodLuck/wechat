package com.project.tool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.project.constans.wecharConstans;


/**
 * <p>Title: HttpsUtil</p>
 * <p>Discription: Https 请求工具 </p>
 * @author 吴敏明
 * @date 2017年12月8日 下午2:05:53
 */
public class HttpsUtil {
	
	/**
	 * HTTPS get请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static final String get(final String url, final Map<String, Object> params) {
        StringBuilder sb = new StringBuilder("");

        if (null != params && !params.isEmpty()) {
            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key).append("=").append(params.get(key));
                i++;
            }
        }

        CloseableHttpClient httpClient = createSSLClientDefault();

        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url + sb.toString());
        String result = "";

        try {
            response = httpClient.execute(get);
            result = getHttpResponseBody(response);
        } catch (IOException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
	
	/**
	 * HTTPS post请求
	 * @param url
	 * @param params
	 * @return
	 */
	public static final String post(final String url, final Map<String, Object> params) {
        CloseableHttpClient httpClient = createSSLClientDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        if (null != params && !params.isEmpty()) {
            post.setEntity(new UrlEncodedFormEntity(paramsConverter(params), Charset.forName("UTF-8")));
        }
        String result = "";
        try {
            response = httpClient.execute(post);
            result = getHttpResponseBody(response);
        } catch (IOException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }
	
	
	/**
	 * 微信支付
	 * @param url
	 * @param params
	 * @return
	 */
	public static final String postXml(final String url, final SortedMap<Object, Object> params) {
        CloseableHttpClient httpClient = createSSLClientDefault();
        // 签名
        String sign = SignUtil.creatSign(params);
        System.out.println("统一下单生成的签名="+sign);
        params.put("sign", sign);
        System.out.println("签名后的map="+params);
        String map2Xml = XMLUtil.map2Xml(params);
        System.out.println("map2Xml="+map2Xml);
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        if (null != params && !params.isEmpty()) {
            post.setEntity(new StringEntity(map2Xml, wecharConstans.CHARSETS));
        }
        String result = "";
        try {
            response = httpClient.execute(post);
            result = getHttpResponseBody(response);
        } catch (IOException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (null != response) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException ex) {
                    Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }
	
	
	
	public static String getHttpResponseBody(HttpResponse response)
			throws UnsupportedOperationException, IOException {
		if (response == null) {
			return null;
		}
		String body = null;
		
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			if (null != entity && entity.isStreaming()) {
				body = EntityUtils.toString(entity, "UTF-8");
			}
		}
		return body;
	}
	
	
	/**
	 * 参数转换
	 * @param params
	 * @return
	 */
	private static List<NameValuePair> paramsConverter(Map<String, Object> params) {
		
		List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair nvp = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            nvpList.add(nvp);
        }
		return nvpList;
	}
	
	
	private static CloseableHttpClient createSSLClientDefault() {

        SSLContext sslContext;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] xcs, String string){
                    return true;
                }
            }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyStoreException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(HttpsUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return HttpClients.createDefault();
    }
	
	/**
	 * 创建SSL安全连接
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private static SSLConnectionSocketFactory createSSLConnSocketFactory(HttpServletRequest request) {
		SSLConnectionSocketFactory sslsf = null;
		try {
			InputStream instream = null;
			KeyStore keyStore = null;
			// 指定读取证书格式为PKCS12
			try {
				keyStore = KeyStore.getInstance("PKCS12");
				// 读取本机存放的PKCS12证书文件/PayPay/src/main/webapp/WEB-INF/apiclient_cert.p12
				ServletContext sc = request.getSession().getServletContext();
				instream = sc.getResourceAsStream("WEB-INF/apiclient_cert.p12");
				// 指定PKCS12的密码(商户ID)
				keyStore.load(instream, "1342228101".toCharArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (instream != null)
						instream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "1342228101".toCharArray()).build();

			sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		return sslsf;
	}

	/**
	 * 发送 SSL POST 请求（HTTPS），JSON形式
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param json
	 *            JSON对象
	 * @return
	 */
	public static String doPostSSL(String apiUrl, Object json, HttpServletRequest request) {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory(request))
				.build();/*
							 * setConnectionManager(connMgr).
							 * setDefaultRequestConfig(requestConfig)
							 */
		HttpPost httpPost = new HttpPost(apiUrl);
		CloseableHttpResponse response = null;
		String httpStr = null;

		try {
//			httpPost.setConfig(RequestConfig.);
			StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");// 解决中文乱码问题
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			/*if (statusCode != HttpStatus.SC_OK) {
				return null;
			}
			if (entity == null) {
				return null;
			}*/
			httpStr = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return httpStr;
	}
}
