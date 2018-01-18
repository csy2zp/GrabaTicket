package com.csy.GrabaTicket.util;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * 保持同一session的HttpClient工具类
 * @author zhangwenchao
 *
 */
@Service
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class HttpKeepSessionUtil {

	private static final Log LOG = LogFactory.getLog(HttpClient.class);
	public  CloseableHttpClient httpClient = null;
	public  HttpClientContext context = null;
	public  CookieStore cookieStore = null;
	public  RequestConfig requestConfig = null;

//	static {
//		init();
//	}
	
	public HttpKeepSessionUtil() {
		init();
	}

	private void init() {
		context = HttpClientContext.create();
		cookieStore = new BasicCookieStore();
		// 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
		requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
				       .setConnectionRequestTimeout(60000).build();
		// 设置默认跳转以及存储cookie
		httpClient = HttpClientBuilder.create()
				     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
				     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
				     .setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * http get
	 * 
	 * @param url
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public byte[] get(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpget, context);
		try {
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				LOG.info("key:" + cookie.getName() + "  value:" + cookie.getValue());
			}
			printResponse(response);
			return EntityUtils.toByteArray(response.getEntity());
		} finally {
			response.close();
		}
		
	}

	/**
	 * http post
	 * 
	 * @param url
	 * @param parameters
	 *            form表单
	 * @return response
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String post(String url, String parameters)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new StringEntity(parameters, "UTF-8"));
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		CloseableHttpResponse response = httpClient.execute(httpPost, context);
		try {
			cookieStore = context.getCookieStore();
			List<Cookie> cookies = cookieStore.getCookies();
			for (Cookie cookie : cookies) {
				LOG.info("key:" + cookie.getName() + "  value:" + cookie.getValue());
			}
			printResponse(response);
			return EntityUtils.toString(response.getEntity(),"UTF-8");
		} finally {
			response.close();
		}
	}

	/**
	 * 手动增加cookie
	 * @param name
	 * @param value
	 * @param domain
	 * @param path
	 */
	public void addCookie(String name, String value, String domain, String path) {
		BasicClientCookie cookie = new BasicClientCookie(name, value);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookieStore.addCookie(cookie);
	}

	/**
	 * 把结果console出来
	 * 
	 * @param httpResponse
	 * @throws ParseException
	 * @throws IOException
	 */
	public void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
		// 获取响应消息实体
//		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		System.out.println("status:" + httpResponse.getStatusLine());
		System.out.println("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			System.out.println("\t" + iterator.next());
		}
		// 判断响应实体是否为空
//		if (entity != null) {
//			String responseString = EntityUtils.toString(entity);
//			System.out.println("response length:" + responseString.length());
//			System.out.println("response content:" + responseString.replace("\r\n", ""));
//		}
		System.out.println(
				"------------------------------------------------------------------------------------------\r\n");
	}

	/**
	 * 把当前cookie从控制台输出出来
	 * 
	 */
	public void printCookies() {
		System.out.println("headers:");
		cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue());
		}
	}

	/**
	 * 检查cookie的键值是否包含传参
	 * 
	 * @param key
	 * @return
	 */
	public boolean checkCookie(String key) {
		cookieStore = context.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();
		boolean res = false;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(key)) {
				res = true;
				break;
			}
		}
		return res;
	}

	/**
	 * 直接把Response内的Entity内容转换成String
	 * 
	 * @param httpResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null)
			return EntityUtils.toString(entity);
		else
			return null;
	}
}

